package aimo.backend.domains.privatePost.dto;

import jakarta.validation.constraints.NotNull;

public record TextRecordRequest(
	@NotNull(message = "제목을 입력하세요.")
	String title,
	@NotNull(message = "내용을 입력하세요.")
	String script
) {
}
