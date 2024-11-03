package aimo.backend.domains.privatePost.dto;

import jakarta.validation.constraints.NotNull;

public record SpeachToTextRequest(
	@NotNull(message = "URL이 필요합니다.")
	String url
) {
}
