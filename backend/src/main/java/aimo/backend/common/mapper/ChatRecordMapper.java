package aimo.backend.common.mapper;

import aimo.backend.domains.privatePost.dto.request.ChatRecordRequest;
import aimo.backend.domains.privatePost.entity.ChatRecord;

public class ChatRecordMapper {
	public static ChatRecord toEntity(String filename, String extension, String script) {
		return ChatRecord
			.builder()
			.filename(filename)
			.extension(extension)
			.script(script)
			.build();
	}
}
