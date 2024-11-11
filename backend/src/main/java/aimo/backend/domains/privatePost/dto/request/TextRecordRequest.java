package aimo.backend.domains.privatePost.dto.request;

import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record TextRecordRequest(
	@NotNull(message = "내용을 입력하세요.")
	String content
) {

	public static TextRecordRequest of(String content) {
		return new TextRecordRequest(content);
	}
}
