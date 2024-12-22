package aimo.backend.domains.ai.dto.parameter;

public record SpeechToTextParameter(String url, Long memberId) {

	public static SpeechToTextParameter of(String url, Long memberId) {
		return new SpeechToTextParameter(url, memberId);
	}
}
