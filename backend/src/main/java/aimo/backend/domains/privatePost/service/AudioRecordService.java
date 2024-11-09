package aimo.backend.domains.privatePost.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.mapper.AudioRecordMapper;
import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.domains.privatePost.dto.request.SaveAudioSuccessRequest;
import aimo.backend.domains.privatePost.dto.response.SaveAudioSuccessResponse;
import aimo.backend.domains.privatePost.dto.request.SpeechToTextRequest;
import aimo.backend.domains.privatePost.dto.response.SpeechToTextResponse;
import aimo.backend.domains.privatePost.entity.AudioRecord;
import aimo.backend.domains.privatePost.repository.AudioRecordRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AudioRecordService {

	private final AudioRecordRepository audioRecordRepository;
	private final WebClient webClient;
	private final AiServerProperties aiServerProperties;

	public SpeechToTextResponse speechToText(SpeechToTextRequest speechToTextRequest) {
		return webClient.post()
			.uri(aiServerProperties.getDomainUrl() + aiServerProperties.getSpeechToTextApi())
			.bodyValue(speechToTextRequest)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
				throw ApiException.from(ErrorCode.AI_BAD_GATEWAY);
			})
			.onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
				throw ApiException.from(ErrorCode.AI_SEVER_ERROR);
			})
			.bodyToMono(new ParameterizedTypeReference<SpeechToTextResponse>() {
			})
			.block();
	}

	@Transactional(rollbackFor = ApiException.class)
	public SaveAudioSuccessResponse save(SaveAudioSuccessRequest saveAudioSuccessRequest) {
		AudioRecord audioRecord = audioRecordRepository.save(AudioRecordMapper.toEntity(saveAudioSuccessRequest));
		return AudioRecordMapper.toSaveAudioSuccessResponse(audioRecord);
	}

	private boolean isAudioFile(String extension) {
		return extension.equals("mp3") || extension.equals("wav") || extension.equals("ogg") || extension.equals("acc")
			|| extension.equals("flac") || extension.equals("m4a") || extension.equals("wma");
	}
}
