package aimo.backend.domains.privatePost.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.mapper.AudioRecordMapper;
import aimo.backend.domains.privatePost.dto.SaveAudioSuccessRequest;
import aimo.backend.domains.privatePost.repository.AudioRecordRepository;
import aimo.backend.infrastructure.s3.S3Service;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AudioRecordService {

	private final AudioRecordRepository audioRecordRepository;
	private final S3Service s3Service;

	public String getPresignedUrl(String fileName) {
		return s3Service.getPresignedUrl("audio", fileName);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void save(SaveAudioSuccessRequest saveAudioSuccessRequest) {
		audioRecordRepository.save(AudioRecordMapper.toEntity(saveAudioSuccessRequest));
	}
}
