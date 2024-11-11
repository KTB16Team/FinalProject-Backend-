package aimo.backend.domains.privatePost.dto.parameter;

import org.springframework.web.multipart.MultipartFile;

import aimo.backend.domains.privatePost.dto.request.ChatRecordRequest;

public record ChatRecordParameter(MultipartFile file) {

	public static ChatRecordParameter from(ChatRecordRequest request) {
		return new ChatRecordParameter(request.file());
	}
}
