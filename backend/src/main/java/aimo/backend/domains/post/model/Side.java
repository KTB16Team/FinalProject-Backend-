package aimo.backend.domains.post.model;

import lombok.Getter;

public enum Side {
	PLAINTIFF("원고"),
	DEFENDANT("피고");

	private final String value;

	Side(String value) {
		this.value = value;
	}
}
