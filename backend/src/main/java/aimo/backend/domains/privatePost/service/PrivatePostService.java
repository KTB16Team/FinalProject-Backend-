package aimo.backend.domains.privatePost.service;

import static aimo.backend.common.exception.ErrorCode.*;

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
import aimo.backend.domains.privatePost.dto.request.JudgementToAiRequest;
import aimo.backend.domains.privatePost.dto.response.JudgementResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostResponse;
import aimo.backend.domains.privatePost.dto.request.SummaryAndJudgementRequest;
import aimo.backend.domains.privatePost.dto.response.JudgementFromAiResponse;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.privatePost.repository.PrivatePostRepository;
import aimo.backend.util.memberLoader.MemberLoader;
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
	private final MemberLoader memberLoader;

	@Transactional(rollbackFor = ApiException.class)
	public JudgementResponse serveScriptToAi(JudgementToAiRequest judgementToAiRequest) {
		Member member = memberLoader.getMember();
		String url = aiServerProperties.getDomainUrl() + aiServerProperties.getJudgementApi();
		log.info("url: {}", url);
		SummaryAndJudgementRequest summaryAndJudgementRequest = new SummaryAndJudgementRequest(
			judgementToAiRequest.content(),
			member.getNickname(),
			member.getGender().getValue(),
			member.getBirthDate());

		log.info("summaryAndJudgementRequest: {}", summaryAndJudgementRequest);

		return webClient.post()
			.uri(url)
			.bodyValue(summaryAndJudgementRequest)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
				log.info("clientResponse: {}", clientResponse);
				throw ApiException.from(ErrorCode.AI_BAD_GATEWAY);
			})
			.onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
				throw ApiException.from(ErrorCode.AI_SEVER_ERROR);
			})
			.bodyToMono(JudgementFromAiResponse.class)
			.map(judgementFromAi -> {
				int faultRateDefendant = judgementFromAi.faultRate().intValue(),
					faultRatePlaintiff = 100 - faultRateDefendant;

				return new JudgementResponse(
					judgementFromAi.title(),
					judgementFromAi.summaryAi(),
					judgementFromAi.stancePlaintiff(),
					judgementFromAi.stanceDefendant(),
					judgementFromAi.judgement(),
					faultRatePlaintiff,
					faultRateDefendant,
					judgementToAiRequest.originType());
			})
			.block();
	}

	@Transactional(rollbackFor = ApiException.class)
	public PrivatePost save(JudgementResponse judgementResponse) {
		Member member = memberLoader.getMember();

		PrivatePost privatePost = PrivatePostMapper.toEntity(judgementResponse, member);

		if (!isValid(member.getId(), privatePost)) {
			throw ApiException.from(PRIVATE_POST_CREATE_UNAUTHORIZED);
		}

		return privatePostRepository.save(privatePost);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void deletePrivatePostBy(Long privatePostId) {
		Member member = memberLoader.getMember();

		PrivatePost privatePost = privatePostRepository.findById(privatePostId)
			.orElseThrow(() -> ApiException.from(ErrorCode.PRIVATE_POST_NOT_FOUND));

		if(!isValid(member.getId(), privatePost)) {
			throw ApiException.from(PRIVATE_POST_DELETE_UNAUTHORIZED);
		}

		member.getPrivatePosts().remove(privatePost);
		privatePostRepository.delete(privatePost);
	}

	public PrivatePostResponse findPrivatePostBy(Long id) {
		PrivatePost privatePost = privatePostRepository.findById(id)
			.orElseThrow(() -> ApiException.from(ErrorCode.PRIVATE_POST_NOT_FOUND));

		if (!isValid(memberLoader.getMember().getId(), privatePost)) {
			throw ApiException.from(PRIVATE_POST_READ_UNAUTHORIZED);
		}

		return PrivatePostMapper.toResponse(privatePost);
	}

	public Page<PrivatePostPreviewResponse> findPrivatePostPreviewsBy(Pageable pageable) {
		return privatePostRepository.findByMemberId(memberLoader.getMemberId(), pageable)
			.map(PrivatePostMapper::toPreviewResponse);
	}

	public void publishPrivatePost(Long privatePostId) {
		PrivatePost privatePost = privatePostRepository.findById(privatePostId)
			.orElseThrow(() -> ApiException.from(PRIVATE_POST_NOT_FOUND));

		if(privatePost.getPublished()) {
			throw ApiException.from(PRIVATE_POST_ALREADY_PUBLISHED);
		}

		privatePost.publish();
	}

	public void unpublishPrivatePost(Long privatePostId){
		PrivatePost privatePost = privatePostRepository.findById(privatePostId)
			.orElseThrow(() -> ApiException.from(PRIVATE_POST_NOT_FOUND));

		if(!privatePost.getPublished()) {
			throw ApiException.from(PRIVATE_POST_ALREADY_UNPUBLISHED);
		}

		privatePost.unpublish();
	}

	private boolean isValid(Long memberId, PrivatePost privatePost) {
		return privatePost.getMember().getId().equals(memberId);
	}
}
