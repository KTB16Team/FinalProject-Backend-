package aimo.backend.domains.vote.model;

import lombok.Getter;

@Getter
public enum Side {
	PLAINTIFF("PLAINTIFF"),
	DEFENDANT("DEFENDANT"),
	NONE("NONE");

	private final String value;

	Side(String value) {
		this.value = value;
	}
}
