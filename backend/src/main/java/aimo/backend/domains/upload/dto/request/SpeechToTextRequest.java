package aimo.backend.domains.upload.dto.request;

import jakarta.validation.constraints.NotNull;

public record SpeechToTextRequest(
	@NotNull(message = "URL이 필요합니다.")
	String url
) {

	public static SpeechToTextRequest of(String url) {
		return new SpeechToTextRequest(url);
	}
}
