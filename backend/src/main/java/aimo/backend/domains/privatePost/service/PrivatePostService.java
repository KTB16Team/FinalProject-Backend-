package aimo.backend.domains.privatePost.service;

import static aimo.backend.common.exception.ErrorCode.*;

import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.mapper.PrivatePostMapper;
import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.privatePost.dto.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.PrivatePostResponse;
import aimo.backend.domains.privatePost.dto.SummaryAndJudgementRequest;
import aimo.backend.domains.privatePost.dto.SummaryAndJudgementResponse;
import aimo.backend.domains.privatePost.dto.TextRecordRequest;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.privatePost.repository.PrivatePostRepository;
import aimo.backend.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivatePostService {

	private final PrivatePostRepository privatePostRepository;
	private final WebClient webClient;
	private final AiServerProperties aiServerProperties;
	private final MemberLoader memberLoader;

	@Transactional(rollbackFor = ApiException.class)
	public SummaryAndJudgementResponse serveScriptToAi(TextRecordRequest textRecordRequest) {
		Member member = memberLoader.getMember();

		SummaryAndJudgementRequest summaryAndJudgementRequest = new SummaryAndJudgementRequest(
			textRecordRequest.title(), textRecordRequest.script(), member.getNickname(), member.getGender(),
			member.getBirthDate());

		return webClient.post()
			.uri(aiServerProperties.getDomainUrl() + aiServerProperties.getJudgementApi())
			.bodyValue(summaryAndJudgementRequest)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
				throw ApiException.from(ErrorCode.AI_BAD_GATEWAY);
			})
			.onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
				throw ApiException.from(ErrorCode.AI_SEVER_ERROR);
			})
			.bodyToMono(new ParameterizedTypeReference<SummaryAndJudgementResponse>() {
			})
			.block();
	}

	@Transactional(rollbackFor = ApiException.class)
	public PrivatePost save(SummaryAndJudgementResponse summaryAndJudgementResponse) {
		PrivatePost privatePost = PrivatePostMapper.toEntity(summaryAndJudgementResponse);

		if (!isValid(memberLoader.getMember().getId(), privatePost)) {
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

	public Page<PrivatePostPreviewResponse> findPrivatePostPreviewsBy(
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<PrivatePost> privatePostPage = privatePostRepository.findAll(pageable);
		Long memberId = memberLoader.getMember().getId();

		return new PageImpl<>(privatePostPage.getContent()
			.stream()
			.map((p) -> {
				if (!isValid(memberId, p))
					throw ApiException.from(PRIVATE_POST_READ_UNAUTHORIZED);
				return PrivatePostMapper.toPreviewResponse(p);
			})
			.collect(Collectors.toList()), pageable, privatePostPage.getTotalPages());
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
