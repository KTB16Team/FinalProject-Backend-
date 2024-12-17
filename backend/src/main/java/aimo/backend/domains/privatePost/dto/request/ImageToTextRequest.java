package aimo.backend.domains.privatePost.dto.request;

import jakarta.validation.constraints.NotNull;

public record ImageToTextRequest(
	@NotNull(message = "URL이 필요합니다.")
	String url
) {

	public static ImageToTextRequest of(String url) {
		return new ImageToTextRequest(url);
	}
}

