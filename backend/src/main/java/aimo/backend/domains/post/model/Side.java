package aimo.backend.domains.post.model;

import lombok.Getter;

public enum Side {
	PLAINTIFF("plaintiff"),
	DEFENDANT("defendant"),
	NONE("none");

	private final String value;

	Side(String value) {
		this.value = value;
	}
}
