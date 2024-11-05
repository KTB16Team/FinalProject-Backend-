package aimo.backend.domains.member.model;

public enum Provider {

	KAKAO("kakao"),
	AIMO("aimo");

	private final String value;

	Provider(String value) {
		this.value = value;
	}
}
