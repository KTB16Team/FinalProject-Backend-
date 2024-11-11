package aimo.backend.domains.privatePost.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.common.util.webclient.ReactiveHttpService;
import aimo.backend.domains.privatePost.dto.parameter.SpeechToTextParameter;
import aimo.backend.domains.privatePost.dto.response.SaveAudioSuccessResponse;
import aimo.backend.domains.privatePost.dto.response.SpeechToTextResponse;
import aimo.backend.domains.privatePost.entity.AudioRecord;
import aimo.backend.domains.privatePost.repository.AudioRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AudioRecordService {

	private final AudioRecordRepository audioRecordRepository;
	private final ReactiveHttpService reactiveHttpService;
	private final AiServerProperties aiServerProperties;

	public SpeechToTextResponse speechToText(SpeechToTextParameter speechToTextParameter) {
		String url = aiServerProperties.getDomainUrl() + aiServerProperties.getSpeechToTextApi();
		return reactiveHttpService.<SpeechToTextParameter, SpeechToTextResponse>post(url, speechToTextParameter).block();
	}

	@Transactional(rollbackFor = ApiException.class)
	public SaveAudioSuccessResponse save(SaveAudioSuccessParameter parameter) {
		AudioRecord audioRecord = audioRecordRepository.save(AudioRecord.from(parameter));
		return SaveAudioSuccessResponse.from(audioRecord);
	}

	private boolean isAudioFile(String extension) {
		return extension.equals("mp3") || extension.equals("wav") || extension.equals("ogg") || extension.equals("acc") || extension.equals("flac") || extension.equals("m4a");
	}
}
