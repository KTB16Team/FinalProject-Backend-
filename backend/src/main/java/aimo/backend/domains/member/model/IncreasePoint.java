package aimo.backend.domains.member.model;

import lombok.Getter;

@Getter
public enum IncreasePoint {

	LIKE(5, 3),
	COMMENT(5, 3),
	POST(20, 3),
	VOTE(5, 3),
	ATTENDANCE(10, 1),
	;

	private final int point;
	private final int limitAttemptCountPerDay;

	IncreasePoint(int point, int limitAttemptCountPerDay) {
		this.point = point;
		this.limitAttemptCountPerDay = limitAttemptCountPerDay;
	}
}
