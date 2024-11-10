package aimo.backend.domains.privatePost.dto.request;

import jakarta.validation.constraints.NotNull;

public record SpeechToTextRequest(
	@NotNull(message = "URL이 필요합니다.")
	String url
) {
}
