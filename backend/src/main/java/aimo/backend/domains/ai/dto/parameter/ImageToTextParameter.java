package aimo.backend.domains.ai.dto.parameter;

public record ImageToTextParameter(String url, Long id) {

	public static ImageToTextParameter from(String url, Long id) {
		return new ImageToTextParameter(url, id);
	}
}
