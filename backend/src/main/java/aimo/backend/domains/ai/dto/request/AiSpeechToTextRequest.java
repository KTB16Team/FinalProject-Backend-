package aimo.backend.domains.ai.dto.request;

public record AiSpeechToTextRequest(
	String url,
	Long id,
	Long privatePostId
) {
}