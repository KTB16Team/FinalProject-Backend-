package aimo.backend.domains.member.model;

import lombok.Getter;

@Getter
public enum PointRule {

	INCREASE_POINT_FROM_LIKE(5),
	INCREASE_POINT_FROM_COMMENT(5),
	INCREASE_POINT_FROM_POST(20),
	INCREASE_POINT_FROM_VOTE(5),
	INCREASE_POINT_FROM_ATTENDANCE(10),
	DECREASE_POINT_FROM_AI_REQUEST(10)
	;

	private final int point;

	PointRule(int point) {
		this.point = point;
	}
}
