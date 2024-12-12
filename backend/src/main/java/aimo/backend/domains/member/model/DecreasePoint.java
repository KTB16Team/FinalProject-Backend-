package aimo.backend.domains.member.model;

import lombok.Getter;

@Getter
public enum DecreasePoint {
	AI_REQUEST(10)
	;

	private final int point;

	DecreasePoint(int point) {
		this.point = point;
	}
}
