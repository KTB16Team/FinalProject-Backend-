package aimo.backend.domains.ai.dto.parameter;

public record ImageToTextParameter(String url, Long memberId) {

	public static ImageToTextParameter from(String url, Long memberId) {
		return new ImageToTextParameter(url, memberId);
	}
}
