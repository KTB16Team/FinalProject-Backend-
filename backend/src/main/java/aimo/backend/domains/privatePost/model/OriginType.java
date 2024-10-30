package aimo.backend.domains.privatePost.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OriginType {
	TEXT("text"),
	VOICE("voice");

	private String value;

	OriginType(String value) {
		this.value = value;
	}

	@JsonCreator
	public static OriginType from(String value) {
		return Arrays.stream(OriginType.values())
			.filter(originType -> originType.value.equals(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown OriginType: " + value));
	}
}
