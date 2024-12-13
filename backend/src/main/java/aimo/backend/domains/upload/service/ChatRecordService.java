package aimo.backend.domains.upload.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.domains.privatePost.dto.parameter.ChatRecordParameter;
import aimo.backend.domains.privatePost.entity.ChatRecord;
import aimo.backend.domains.privatePost.repository.ChatRecordRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRecordService {

	private final ChatRecordRepository chatRecordRepository;

	@Transactional(rollbackFor = ApiException.class)
	public Long save(ChatRecordParameter chatRecordParameter) throws IOException {
		String originalFilename = chatRecordParameter.file().getOriginalFilename();
		if (originalFilename == null)
			throw ApiException.from(ErrorCode.INVALID_FILE_NAME);

		List<String> parts = Arrays
			.stream(originalFilename.split("\\."))
			.toList();

		if (parts.size() <= 1) throw ApiException.from(ErrorCode.INVALID_FILE_NAME);

		String filename = String.join(".", parts.subList(0, parts.size() - 1));
		String extension = parts.get(parts.size() - 1);

		if (!Objects.equals(extension, "txt")) throw ApiException.from(ErrorCode.INVALID_FILE_EXTENSION);

		String record = StreamUtils
			.copyToString(chatRecordParameter.file().getInputStream(), StandardCharsets.UTF_8);
		ChatRecord chatRecord = ChatRecord.of(filename, extension, record);

		return chatRecordRepository.save(chatRecord).getId();
	}

	public ChatRecord findById(Long chatRecordId) {
		return chatRecordRepository.findById(chatRecordId)
			.orElseThrow(() -> ApiException.from(ErrorCode.CHAT_RECORD_NOT_FOUND));
	}
}
