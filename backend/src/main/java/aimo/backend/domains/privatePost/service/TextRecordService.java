package aimo.backend.domains.privatePost.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.mapper.TextRecordMapper;
import aimo.backend.domains.privatePost.dto.request.TextRecordRequest;
import aimo.backend.domains.privatePost.entity.TextRecord;
import aimo.backend.domains.privatePost.repository.TextRecordRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TextRecordService {

	private final TextRecordRepository textRecordRepository;

	@Transactional(rollbackFor = Exception.class)
	public void save(TextRecordRequest textRecordRequest) {
		TextRecord textRecord = TextRecordMapper.toEntity(textRecordRequest);
		textRecordRepository.save(textRecord);
	}
}
