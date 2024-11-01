package aimo.backend.domains.privatePost.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.privatePost.dto.TextRecordRequest;
import aimo.backend.domains.privatePost.entity.TextRecord;
import aimo.backend.domains.privatePost.repository.TextRecordRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TextRecordService {

	private final TextRecordRepository textRecordRepository;

	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<DataResponse<Void>> save(TextRecordRequest textRecordRequest) {
		TextRecord textRecord = TextRecord
			.builder()
			.title(textRecordRequest.title())
			.script(textRecordRequest.script())
			.build();

		textRecordRepository.save(textRecord);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}
}
