package aimo.backend.domains.privatePost.service;

import static aimo.backend.common.exception.ErrorCode.*;

import javax.swing.text.html.parser.Entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.mapper.PrivatePostMapper;
import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.service.MemberService;
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
import aimo.backend.domains.privatePost.repository.PrivatePostRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivatePostService {

	private final PrivatePostRepository privatePostRepository;
	private final WebClient webClient;
	private final AiServerProperties aiServerProperties;
	private final EntityManager em;

	// AI 서버에 판단 요청
	@Transactional(rollbackFor = ApiException.class)
	public JudgementResponse serveScriptToAi(JudgementToAiParameter parameter) {
		Member member = em.getReference(Member.class, parameter.memberId());
		String url = aiServerProperties.getDomainUrl() + aiServerProperties.getJudgementApi();

		SummaryAndJudgementRequest summaryAndJudgementRequest =
			PrivatePostMapper.toSummaryAndJudgementRequest(parameter, member);

		return webClient.post()
			.uri(url)
			.bodyValue(summaryAndJudgementRequest)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
				throw ApiException.from(ErrorCode.AI_BAD_GATEWAY);
			})
			.onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
				throw ApiException.from(ErrorCode.AI_SEVER_ERROR);
			})
			.bodyToMono(JudgementFromAiResponse.class)
			.map(judgementFromAi -> {
				int faultRateDefendant = judgementFromAi.faultRate().intValue(), faultRatePlaintiff = 100 - faultRateDefendant;
				return PrivatePostMapper.toJudgementResponse(judgementFromAi, faultRatePlaintiff, faultRateDefendant, parameter);
			})
			.block();
	}

	// 개인글 저장
	@Transactional(rollbackFor = ApiException.class)
	public Long save(JudgementParameter judgementParameter) {
		Long memberId = judgementParameter.memberId();
		Member member = em.getReference(Member.class, memberId);

		PrivatePost privatePost = PrivatePostMapper.toEntity(judgementParameter, member);

		return privatePostRepository.save(privatePost).getId();
	}

	// 개인글 삭제
	@Transactional(rollbackFor = ApiException.class)
	public void deletePrivatePostBy(DeletePrivatePostParameter parameter) {
		Long memberId = parameter.memberId();
		Long privatePostId = parameter.privatePostId();
		Member member = em.getReference(Member.class, memberId);

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

		return PrivatePostMapper.toResponse(privatePost);
	}

	// 개인글 목록 조회
	public Page<PrivatePostPreviewResponse> findPrivatePostPreviewsBy(FindPrivatePostPreviewParameter parameter) {
		return privatePostRepository.findByMemberId(parameter.memberId(), parameter.pageable())
			.map(PrivatePostMapper::toPreviewResponse);
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
