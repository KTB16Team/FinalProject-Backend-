package aimo.backend.domains.privatePost.dto.parameter;

public record ImageToTextParameter(String url) {

	public static ImageToTextParameter from(String url) {
		return new ImageToTextParameter(url);
	}
}
