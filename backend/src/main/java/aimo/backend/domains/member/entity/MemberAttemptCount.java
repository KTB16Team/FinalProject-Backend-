package aimo.backend.domains.member.entity;

import static lombok.AccessLevel.*;

import org.hibernate.annotations.ColumnDefault;

import aimo.backend.domains.member.model.IncreasePoint;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class MemberAttemptCount {

	@Id
	private Long memberId;

	@ColumnDefault("0")
	private int likeCount = 0;

	@ColumnDefault("0")
	private int commentCount = 0;

	@ColumnDefault("0")
	private int voteCount = 0;

	@ColumnDefault("0")
	private int postCount = 0;

	@ColumnDefault("0")
	private int attendanceCount = 0;

	public static MemberAttemptCount createBasic(Long memberId) {
		return new MemberAttemptCount(memberId, 0, 0, 0, 0, 0);
	}

	public boolean canReceivePoint(IncreasePoint increasePoint) {
		if (increasePoint == IncreasePoint.VOTE && voteCount < increasePoint.getLimitAttemptCountPerDay()) {
			return true;
		}
		if (increasePoint == IncreasePoint.ATTENDANCE && attendanceCount < increasePoint.getLimitAttemptCountPerDay()) {
			return true;
		}
		if (increasePoint == IncreasePoint.LIKE && likeCount < increasePoint.getLimitAttemptCountPerDay()) {
			return true;
		}
		if (increasePoint == IncreasePoint.COMMENT && commentCount < increasePoint.getLimitAttemptCountPerDay()) {
			return true;
		}

		if (increasePoint == IncreasePoint.POST && postCount < increasePoint.getLimitAttemptCountPerDay()) {
			return true;
		}
		return false;
	}

	public void increaseCount(IncreasePoint increasePoint) {
		if (increasePoint == IncreasePoint.VOTE) {
			voteCount++;
		}
		if (increasePoint == IncreasePoint.ATTENDANCE) {
			attendanceCount++;
		}
		if (increasePoint == IncreasePoint.LIKE) {
			likeCount++;
		}
		if (increasePoint == IncreasePoint.COMMENT) {
			commentCount++;
		}
		if (increasePoint == IncreasePoint.POST) {
			postCount++;
		}
	}
}
