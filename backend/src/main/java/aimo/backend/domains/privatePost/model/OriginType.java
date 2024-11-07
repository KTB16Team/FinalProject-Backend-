package aimo.backend.domains.privatePost.model;

public enum OriginType {

	TEXT("text"),
	VOICE("voice"),
	CHAT("chat");

	private String value;

	OriginType(String value) {
		this.value = value;
	}
}
