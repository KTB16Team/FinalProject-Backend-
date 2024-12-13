package aimo.backend.domains.upload.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.common.util.webclient.ReactiveHttpService;
import aimo.backend.domains.upload.dto.parameter.CreateAudioPreSignedUrlParameter;
import aimo.backend.domains.upload.dto.parameter.SaveAudioMetaDataParameter;
import aimo.backend.domains.upload.dto.parameter.SpeechToTextParameter;
import aimo.backend.domains.upload.dto.response.SaveAudioMetaDataResponse;
import aimo.backend.domains.privatePost.dto.response.SpeechToTextResponse;
import aimo.backend.domains.privatePost.entity.AudioRecord;
import aimo.backend.domains.privatePost.repository.AudioRecordRepository;

import aimo.backend.infrastructure.s3.S3Service;
import aimo.backend.infrastructure.s3.dto.response.CreatePreSignedUrlResponse;
import aimo.backend.infrastructure.s3.model.PresignedUrlPrefix;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AudioRecordService {

	private final AudioRecordRepository audioRecordRepository;
	private final ReactiveHttpService reactiveHttpService;
	private final AiServerProperties aiServerProperties;
	private final S3Service s3Service;

	// AI서버에 음성 파일을 텍스트로 변환 요청
	public SpeechToTextResponse speechToText(SpeechToTextParameter speechToTextParameter) {
		String url = aiServerProperties.getDomainUrl() + aiServerProperties.getSpeechToTextApi();
		return reactiveHttpService.<SpeechToTextParameter, SpeechToTextResponse>post(url, speechToTextParameter).block();
	}

	// 음성 파일 메타데이터 저장
	@Transactional(rollbackFor = ApiException.class)
	public SaveAudioMetaDataResponse save(SaveAudioMetaDataParameter parameter) {
		AudioRecord audioRecord = audioRecordRepository.save(AudioRecord.from(parameter));
		return SaveAudioMetaDataResponse.from(audioRecord);
	}

	public CreatePreSignedUrlResponse createAudioPreSignedUrl(CreateAudioPreSignedUrlParameter parameter) {
		if (!isAudioFile(parameter.extension())) {
			throw ApiException.from(ErrorCode.INVALID_FILE_EXTENSION);
		}

		return s3Service.createPreSignedUrl(
			parameter.filename(),
			PresignedUrlPrefix.AUDIO
		);
	}

	private boolean isAudioFile(String extension) {
		return extension.equals("mp3")
			|| extension.equals("wav")
			|| extension.equals("ogg")
			|| extension.equals("acc")
			|| extension.equals("flac")
			|| extension.equals("m4a");
	}
}
