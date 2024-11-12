package aimo.backend.domains.privatePost.model;

import lombok.Getter;

@Getter
public enum ContentLength {

	PREVIEW_LENGTH(90),
	;
	private final int value;

	ContentLength(int value) {
		this.value = value;
	}
}
