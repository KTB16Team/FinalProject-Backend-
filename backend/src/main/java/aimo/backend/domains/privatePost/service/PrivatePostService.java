package aimo.backend.domains.privatePost.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.mapper.DisputeMapper;
import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.domains.privatePost.dto.SummaryAndJudgementRequest;
import aimo.backend.domains.privatePost.dto.SummaryAndJudgementResponse;
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
		SummaryAndJudgementRequest summaryAndJudgementRequest
	) {
		return webClient
				.post()
				.uri(aiServerProperties.getDomainUrl() + aiServerProperties.getJudgementApi())
				.bodyValue(summaryAndJudgementRequest)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
					throw ApiException.from(ErrorCode.AI_BAD_GATEWAY);
				})
				.onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
					throw ApiException.from(ErrorCode.AI_SEVER_ERROR);
				})
				.bodyToMono(new ParameterizedTypeReference<DataResponse<SummaryAndJudgementResponse>>() {})
				.flatMap(response -> saveDispute(response.getData()))
				.then(Mono.just(DataResponse.created()));
	}

	@Transactional(rollbackFor = ApiException.class)
	public Mono<PrivatePost> saveDispute(SummaryAndJudgementResponse summaryAndJudgementResponse) {
		PrivatePost privatePost = DisputeMapper.toEntity(summaryAndJudgementResponse);
		return Mono.fromCallable(() -> privatePostRepository.save(privatePost))
			.subscribeOn(Schedulers.boundedElastic());
	}
}
