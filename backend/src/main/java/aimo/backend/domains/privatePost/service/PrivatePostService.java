package aimo.backend.domains.privatePost.service;

import static aimo.backend.common.exception.ErrorCode.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.amqp.AmqpException;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.dto.PageResponse;
import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.messageQueue.MessageQueueService;
import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.DecreasePoint;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.member.service.MemberPointService;
import aimo.backend.domains.privatePost.dto.parameter.DeletePrivatePostParameter;
import aimo.backend.domains.privatePost.dto.parameter.FindPrivatePostParameter;
import aimo.backend.domains.privatePost.dto.parameter.FindPrivatePostPreviewParameter;
import aimo.backend.domains.privatePost.dto.parameter.JudgementToAiParameter;
import aimo.backend.domains.privatePost.dto.parameter.UpdateContentToPrivatePostParameter;
import aimo.backend.domains.privatePost.dto.parameter.UpdatePostContentParameter;
import aimo.backend.domains.privatePost.dto.request.SummaryAndJudgementRequest;
import aimo.backend.domains.privatePost.dto.response.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostResponse;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.privatePost.entity.TextRecord;
import aimo.backend.domains.privatePost.model.PrivatePostStatus;
import aimo.backend.domains.privatePost.repository.PrivatePostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivatePostService {

	private final PrivatePostRepository privatePostRepository;
	private final MemberRepository memberRepository;
	private final MessageQueueService messageQueueService;
	private final AiServerProperties aiServerProperties;
	private final MemberPointService memberPointService;

	// mq에 판단 요청
	@Async
	@Transactional(rollbackFor = {ApiException.class, AmqpException.class})
	public void uploadTextRecordAndRequestJudgement(JudgementToAiParameter parameter) {
		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		// db에 저장
		TextRecord textRecord = TextRecord.of(parameter.content());
		PrivatePost privatePost = PrivatePost.createWithoutContent(member, textRecord, parameter.originType());
		privatePostRepository.save(privatePost);

		// mq에 요청
		SummaryAndJudgementRequest request = SummaryAndJudgementRequest.from(
			privatePost.getId(),
			parameter.content(),
			member);
		messageQueueService.send(request);
	}

	// AI로부터 받은 콜백 응답을 기존에 저장했던 PrivatePost에 업데이트
	@Transactional
	public void updateContentToPrivatePost(UpdateContentToPrivatePostParameter parameter) {
		// 키 확인
		validateKey(parameter.accessKey());

		Long privatePostId = parameter.id();
		PrivatePost privatePost = privatePostRepository.findById(privatePostId)
			.orElseThrow(() -> ApiException.from(ErrorCode.PRIVATE_POST_NOT_FOUND));

		// 실패시
		if (!parameter.status()) {
			privatePost.updateStatus(PrivatePostStatus.FAIL);
			return;
		}

		// 성공시
		Integer faultRatePlaintiff = parameter.faultRate().intValue();
		Integer faultRateDefendant = calculateFaultRateDefendant(parameter.faultRate());

		UpdatePostContentParameter updatePostContentParameter = new UpdatePostContentParameter(
			parameter.stancePlaintiff(),
			parameter.stanceDefendant(),
			parameter.title(),
			parameter.summaryAi(),
			parameter.judgement(),
			faultRatePlaintiff,
			faultRateDefendant
		);

		privatePost.updateContent(updatePostContentParameter);
		privatePost.updateStatus(PrivatePostStatus.SUCCESS);

		// 포인트 감소
		memberPointService.decreaseMemberPoint(privatePost.getMember().getId(), DecreasePoint.AI_REQUEST);
	}

	// 키 확인
	private void validateKey(String key) {
		if (!aiServerProperties.getAccessKey().equals(key)) {
			throw ApiException.from(ErrorCode.INVALID_ACCESS_KEY);
		}
	}

	// 과실 비율 계산
	private Integer calculateFaultRateDefendant(Float faultRate) {
		return 100 - faultRate.intValue();
	}

	// 개인글 삭제
	@Transactional(rollbackFor = ApiException.class)
	public void deletePrivatePostBy(DeletePrivatePostParameter parameter) {
		Long memberId = parameter.memberId();
		Long privatePostId = parameter.privatePostId();

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));
		PrivatePost privatePost = privatePostRepository.findById(privatePostId)
			.orElseThrow(() -> ApiException.from(ErrorCode.PRIVATE_POST_NOT_FOUND));

		if (!validationPrivatePost(memberId, privatePost)) {
			throw ApiException.from(PRIVATE_POST_DELETE_UNAUTHORIZED);
		}

		member.getPrivatePosts().remove(privatePost);
		privatePostRepository.delete(privatePost);
	}

	// 개인글 단일 조회
	// 에러 반환시 롤백하면 안된다.
	@Transactional(noRollbackFor = ApiException.class)
	public PrivatePostResponse findPrivatePostResponseBy(FindPrivatePostParameter parameter) {
		PrivatePost privatePost = privatePostRepository.findByMember_IdAndId(
				parameter.memberId(),
				parameter.privatePostId())
			.orElseThrow(() -> ApiException.from(ErrorCode.PRIVATE_POST_NOT_FOUND));

		// 상태 확인 및 에러시 처리
		validatePrivatePostStatus(privatePost);

		return PrivatePostResponse.from(privatePost);
	}

	// 개인글 상태에 따른 에러 리턴 및 실패시 개인글 삭제
	public void validatePrivatePostStatus(PrivatePost privatePost) {
		// 분석 10분이 지난 경우 삭제하고 에러 반환
		LocalDateTime createdAt = privatePost.getCreatedAt();
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(createdAt, now);
		if (duration.toMinutes() > 10 && privatePost.getPrivatePostStatus() == PrivatePostStatus.PROGRESS) {
			privatePostRepository.delete(privatePost);
			throw ApiException.from(PRIVATE_POST_FAIL);
		}

		// 아직 분석중인 경우
		if (privatePost.getPrivatePostStatus() == PrivatePostStatus.PROGRESS) {
			throw ApiException.from(PRIVATE_POST_PROGRESS);
		}

		// 분석 실패한 경우
		if (privatePost.getPrivatePostStatus() == PrivatePostStatus.FAIL) {
			privatePostRepository.delete(privatePost);
			throw ApiException.from(PRIVATE_POST_FAIL);
		}
	}

	// 개인글 목록 조회
	public PageResponse<PrivatePostPreviewResponse> findPrivatePostPreviewsBy(
		FindPrivatePostPreviewParameter parameter) {
		Page<PrivatePostPreviewResponse> privatePosts = privatePostRepository.findByMemberId(parameter.memberId(),
				parameter.pageable())
			.map(PrivatePostPreviewResponse::from);

		return PageResponse.from(privatePosts);
	}

	// 개인글 공개 처리
	public void publishPrivatePost(Long privatePostId) {
		PrivatePost privatePost = privatePostRepository.findById(privatePostId)
			.orElseThrow(() -> ApiException.from(PRIVATE_POST_NOT_FOUND));

		if (privatePost.getPublished())
			throw ApiException.from(PRIVATE_POST_ALREADY_PUBLISHED);

		privatePost.publish();
	}

	// 개인글 비공개 처리
	public void unpublishPrivatePost(Long privatePostId) {
		PrivatePost privatePost = privatePostRepository.findById(privatePostId)
			.orElseThrow(() -> ApiException.from(PRIVATE_POST_NOT_FOUND));

		if (!privatePost.getPublished()) {
			throw ApiException.from(PRIVATE_POST_ALREADY_UNPUBLISHED);
		}

		privatePost.unpublish();
	}

	// 유효성 검증
	private boolean validationPrivatePost(Long memberId, PrivatePost privatePost) {
		return privatePost.getMember().getId().equals(memberId);
	}


}
