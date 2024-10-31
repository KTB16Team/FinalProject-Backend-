package aimo.backend.domains.privatePost.service;

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

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.mapper.PrivatePostMapper;
import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.domains.privatePost.dto.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.PrivatePostResponse;
import aimo.backend.domains.privatePost.dto.SummaryAndJudgementResponse;
import aimo.backend.domains.privatePost.dto.TextRecordRequest;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.privatePost.repository.PrivatePostRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivatePostService {

	private final PrivatePostRepository privatePostRepository;
	private final WebClient webClient;
	private final AiServerProperties aiServerProperties;

	@Transactional(rollbackFor = ApiException.class)
	public Mono<DataResponse<Void>> serveScriptToAi(
		TextRecordRequest textRecordRequest
	) {
		return webClient
				.post()
				.uri(aiServerProperties.getDomainUrl() + aiServerProperties.getJudgementApi())
				.bodyValue(textRecordRequest)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
					throw ApiException.from(ErrorCode.AI_BAD_GATEWAY);
				})
				.onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
					throw ApiException.from(ErrorCode.AI_SEVER_ERROR);
				})
				.bodyToMono(new ParameterizedTypeReference<DataResponse<SummaryAndJudgementResponse>>() {})
				.flatMap(response -> save(response.getData()))
				.then(Mono.just(DataResponse.created()));
	}

	@Transactional(rollbackFor = ApiException.class)
	public Mono<PrivatePost> save(SummaryAndJudgementResponse summaryAndJudgementResponse) {
		PrivatePost privatePost = PrivatePostMapper.toEntity(summaryAndJudgementResponse);
		return Mono.fromCallable(() -> privatePostRepository.save(privatePost))
			.subscribeOn(Schedulers.boundedElastic());
	}

	public PrivatePostResponse getPrivatePost(Long id) {
		PrivatePost privatePost = privatePostRepository
			.findById(id)
			.orElseThrow(() -> ApiException.from(ErrorCode.PRIVATE_POST_NOT_FOUND));

		return PrivatePostMapper.toResponse(privatePost);
	}

	public Page<PrivatePostPreviewResponse> getPrivatePostPreviews(
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		Page<PrivatePost> privatePostPage = privatePostRepository
			.findAll(pageable);

		return new PageImpl<> (
			privatePostPage
			.getContent()
			.stream()
			.map(PrivatePostMapper::toPreviewResponse)
			.collect(Collectors.toList()),
			pageable,
			privatePostPage.getTotalPages()
			);
	}
}
