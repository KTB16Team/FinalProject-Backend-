package aimo.backend.domains.ai.dto.parameter;

import aimo.backend.domains.ai.dto.request.SpeechToTextRequest;

public record SpeechToTextParameter(String url) {

	public static SpeechToTextParameter of(String url) {
		return new SpeechToTextParameter(url);
	}

	public static SpeechToTextParameter from(SpeechToTextRequest request) {
		return new SpeechToTextParameter(request.url());
	}
}
