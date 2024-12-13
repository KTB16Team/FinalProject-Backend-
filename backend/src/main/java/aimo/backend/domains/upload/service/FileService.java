package aimo.backend.domains.upload.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.common.util.webclient.ReactiveHttpService;
import aimo.backend.domains.upload.dto.parameter.CreateFilePreSignedUrlParameter;
import aimo.backend.domains.upload.dto.parameter.SaveFileMetaDataParameter;
import aimo.backend.domains.upload.dto.parameter.SpeechToTextParameter;
import aimo.backend.domains.upload.dto.response.SaveFileMetaDataResponse;
import aimo.backend.domains.privatePost.dto.response.SpeechToTextResponse;
import aimo.backend.domains.upload.entity.FileRecord;
import aimo.backend.domains.upload.repository.FileRepository;

import aimo.backend.infrastructure.s3.S3Service;
import aimo.backend.infrastructure.s3.dto.response.CreatePreSignedUrlResponse;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

	private final FileRepository fileRepository;
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
	public SaveFileMetaDataResponse save(SaveFileMetaDataParameter parameter) {
		FileRecord fileRecord = fileRepository.save(FileRecord.from(parameter));
		return SaveFileMetaDataResponse.from(fileRecord);
	}

	public CreatePreSignedUrlResponse createFilePreSignedUrl(CreateFilePreSignedUrlParameter parameter) {
		// Prefix 타입과 extention 일치하는 지 확인
		if (!parameter.prefix().isValidExtension(parameter.extension())) {
			throw ApiException.from(ErrorCode.INVALID_FILE_EXTENSION);
		}

		return s3Service.createPreSignedUrl(
			parameter.filename(),
			parameter.prefix()
		);
	}

}
