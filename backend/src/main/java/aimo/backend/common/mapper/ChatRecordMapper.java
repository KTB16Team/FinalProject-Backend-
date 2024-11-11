package aimo.backend.common.mapper;

import aimo.backend.domains.privatePost.entity.ChatRecord;

public class ChatRecordMapper {
	public static ChatRecord toEntity(String filename, String extension, String content) {
		return ChatRecord
			.builder()
			.filename(filename)
			.extension(extension)
			.content(content)
			.build();
	}
}
