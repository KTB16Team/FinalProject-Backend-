package aimo.backend.domains.privatePost.dto.response;

public record SpeechToTextResponse(
	String record
) {

	public static SpeechToTextResponse of(String record) {
		return new SpeechToTextResponse(record);
	}
}
