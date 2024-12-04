package aimo.backend.domains.privatePost.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.messageQueue.MessageQueueService;
import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.common.service.ExternalApiService;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.privatePost.dto.parameter.DeletePrivatePostParameter;
import aimo.backend.domains.privatePost.dto.parameter.FindPrivatePostParameter;
import aimo.backend.domains.privatePost.dto.parameter.FindPrivatePostPreviewParameter;
import aimo.backend.domains.privatePost.dto.parameter.JudgementParameter;
import aimo.backend.domains.privatePost.dto.parameter.JudgementToAiParameter;
import aimo.backend.domains.privatePost.dto.response.JudgementResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostResponse;
import aimo.backend.domains.privatePost.dto.request.SummaryAndJudgementRequest;
import aimo.backend.domains.privatePost.dto.response.JudgementFromAiResponse;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.privatePost.entity.TextRecord;
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
	private final AiServerProperties aiServerProperties;
	private final ExternalApiService externalApiService;
	private final MessageQueueService messageQueueService;

	@Transactional
	public Long uploadTextRecordAndRequestJudgement(JudgementToAiParameter parameter) {
		Long memberId = parameter.memberId();
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		JudgementResponse judgementResponse = requestJudgementToAi(parameter, member);
		JudgementParameter judgementParameter = JudgementParameter.from(memberId, judgementResponse);
		TextRecord textRecord = TextRecord.of(parameter.content());
		PrivatePost privatePost = PrivatePost.from(judgementParameter, member, textRecord);

		return privatePostRepository.save(privatePost).getId();
	}

	// AI 서버에 판단 요청
	private JudgementResponse requestJudgementToAi(JudgementToAiParameter parameter, Member member) {
		String url = aiServerProperties.getDomainUrl() + aiServerProperties.getJudgementApi();

		SummaryAndJudgementRequest summaryAndJudgementRequest =
			SummaryAndJudgementRequest.from(parameter, member);

		return externalApiService
			.post(url, summaryAndJudgementRequest, JudgementFromAiResponse.class)
			.map(judgementFromAi -> mapToJudgementResponse(judgementFromAi, parameter))
			.block();
	}

	// mq에 판단 요청
	@Async
	@Transactional
	public void uploadTextRecordAndRequestJudgementV2(JudgementToAiParameter parameter) {
		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		TextRecord textRecord = TextRecord.of(parameter.content());
		PrivatePost privatePost = PrivatePost.createWithoutContent(member, textRecord, parameter.originType());
		privatePostRepository.save(privatePost);

		SummaryAndJudgementRequest request = SummaryAndJudgementRequest.from(parameter, member);
		messageQueueService.send(request);
	}

	private JudgementResponse mapToJudgementResponse(JudgementFromAiResponse judgementFromAi, JudgementToAiParameter parameter) {
		int faultRateDefendant = judgementFromAi.faultRate().intValue(),
			faultRatePlaintiff = 100 - faultRateDefendant;
		return JudgementResponse.from(judgementFromAi, faultRatePlaintiff, faultRateDefendant, parameter);
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

		if(!validationPrivatePost(memberId, privatePost)) {
			throw ApiException.from(PRIVATE_POST_DELETE_UNAUTHORIZED);
		}

		member.getPrivatePosts().remove(privatePost);
		privatePostRepository.delete(privatePost);
	}

	// 개인글 단일 조회
	public PrivatePostResponse findPrivatePostResponseBy(FindPrivatePostParameter parameter) {
		PrivatePost privatePost = privatePostRepository.findById(parameter.privatePostId())
			.orElseThrow(() -> ApiException.from(ErrorCode.PRIVATE_POST_NOT_FOUND));

		if (!validationPrivatePost(parameter.memberId(), privatePost))
			throw ApiException.from(PRIVATE_POST_READ_UNAUTHORIZED);

		return PrivatePostResponse.from(privatePost);
	}

	// 개인글 목록 조회
	public Page<PrivatePostPreviewResponse> findPrivatePostPreviewsBy(FindPrivatePostPreviewParameter parameter) {
		return privatePostRepository.findByMemberId(parameter.memberId(), parameter.pageable())
			.map(PrivatePostPreviewResponse::from);
	}

	// 개인글 공개 처리
	public void publishPrivatePost(Long privatePostId) {
		PrivatePost privatePost = privatePostRepository.findById(privatePostId)
			.orElseThrow(() -> ApiException.from(PRIVATE_POST_NOT_FOUND));

		if(privatePost.getPublished())
			throw ApiException.from(PRIVATE_POST_ALREADY_PUBLISHED);

		privatePost.publish();
	}

	// 개인글 비공개 처리
	public void unpublishPrivatePost(Long privatePostId){
		PrivatePost privatePost = privatePostRepository.findById(privatePostId)
			.orElseThrow(() -> ApiException.from(PRIVATE_POST_NOT_FOUND));

		if(!privatePost.getPublished()) {
			throw ApiException.from(PRIVATE_POST_ALREADY_UNPUBLISHED);
		}

		privatePost.unpublish();
	}

	// 유효성 검증
	private boolean validationPrivatePost(Long memberId, PrivatePost privatePost) {
		return privatePost.getMember().getId().equals(memberId);
	}


}
