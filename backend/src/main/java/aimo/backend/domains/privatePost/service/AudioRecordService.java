package aimo.backend.domains.privatePost.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.mapper.AudioRecordMapper;
import aimo.backend.domains.privatePost.dto.SaveAudioSuccessRequest;
import aimo.backend.domains.privatePost.dto.SaveAudioSuccessResponse;
import aimo.backend.domains.privatePost.entity.AudioRecord;
import aimo.backend.domains.privatePost.repository.AudioRecordRepository;
import aimo.backend.infrastructure.s3.S3Service;
import aimo.backend.infrastructure.s3.dto.CreatePresignedUrlRequest;
import aimo.backend.infrastructure.s3.dto.CreatePresignedUrlResponse;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AudioRecordService {

	private final AudioRecordRepository audioRecordRepository;
	private final S3Service s3Service;

	public CreatePresignedUrlResponse createPresignedUrl(CreatePresignedUrlRequest createPresignedUrlRequest) {
		String url = s3Service.createAudioPreSignedUrl(createPresignedUrlRequest);
		String filename = createPresignedUrlRequest.filename();
		String extension = filename.substring(filename.lastIndexOf(".") + 1);

		if(!isAudioFile(extension)) {
			throw ApiException.from(ErrorCode.INVALID_FILE_EXTENSION);
		}

		return new CreatePresignedUrlResponse(url, filename);
	}

	@Transactional(rollbackFor = ApiException.class)
	public SaveAudioSuccessResponse save(SaveAudioSuccessRequest saveAudioSuccessRequest) {
		AudioRecord audioRecord = audioRecordRepository.save(AudioRecordMapper.toEntity(saveAudioSuccessRequest));
		return AudioRecordMapper.toSaveAudioSuccessResponse(audioRecord);
	}

	private boolean isAudioFile(String extension) {
		return extension.equals("mp3") || extension.equals("wav") || extension.equals("ogg") || extension.equals("acc") || extension.equals("flac");
	}
}
