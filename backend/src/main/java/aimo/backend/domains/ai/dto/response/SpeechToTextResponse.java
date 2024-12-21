package aimo.backend.domains.ai.dto.response;

public record SpeechToTextResponse(
	String script
) {

	public static SpeechToTextResponse of(String script) {
		return new SpeechToTextResponse(script);
	}
}
