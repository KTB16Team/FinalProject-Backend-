package aimo.backend.domains.ai.dto.parameter;

public record SpeechToTextParameter(String url, Long id) {

	public static SpeechToTextParameter of(String url, Long id) {
		return new SpeechToTextParameter(url, id);
	}
}
