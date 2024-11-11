package aimo.backend.domains.privatePost.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.privatePost.dto.parameter.TextRecordParameter;
import aimo.backend.domains.privatePost.entity.TextRecord;
import aimo.backend.domains.privatePost.repository.TextRecordRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TextRecordService {

	private final TextRecordRepository textRecordRepository;

	@Transactional(rollbackFor = Exception.class)
	public Long save(TextRecordParameter parameter) {
		TextRecord textRecord = TextRecord.from(parameter);
		return textRecordRepository.save(textRecord).getId();
	}

	public TextRecord findById(Long id) {
		return textRecordRepository.findById(id).orElseThrow(() -> ApiException.from(TEXT_RECORD_NOT_FOUND));
	}
}
