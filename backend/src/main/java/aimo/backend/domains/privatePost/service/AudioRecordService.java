package aimo.backend.domains.privatePost.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.mapper.AudioRecordMapper;
import aimo.backend.domains.privatePost.dto.SaveAudioSuccessRequest;
import aimo.backend.domains.privatePost.repository.AudioRecordRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AudioRecordService {

	private final AudioRecordRepository audioRecordRepository;

	@Transactional(rollbackFor = ApiException.class)
	public void save(SaveAudioSuccessRequest saveAudioSuccessRequest) {
		audioRecordRepository.save(AudioRecordMapper.toEntity(saveAudioSuccessRequest));
	}
}
