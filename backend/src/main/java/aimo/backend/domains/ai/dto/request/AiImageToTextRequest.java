package aimo.backend.domains.ai.dto.request;

public record AiImageToTextRequest(
	String url,
	Long id,
	Long privatePostId
) {
}
