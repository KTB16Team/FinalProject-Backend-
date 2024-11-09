package aimo.backend.domains.privatePost.dto.request;

import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record TextRecordRequest(
	@NotNull(message = "제목을 입력하세요.")
	String title,
	@NotNull(message = "내용을 입력하세요.")
	String script,
	@Enumerated(EnumType.STRING)
	@NotNull(message = "원본 타입을 입력하세요. (TEXT, CHAT, VOICE)")
	OriginType originalType
) {
}
