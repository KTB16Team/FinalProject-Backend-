package aimo.backend.domains.privatePost.model;

public enum OriginType {
	TEXT("text"),
	VOICE("voice"),
	CHAT("chat"),
	IMAGE("image");

	private String value;

	OriginType(String value) {
		this.value = value;
	}
}
