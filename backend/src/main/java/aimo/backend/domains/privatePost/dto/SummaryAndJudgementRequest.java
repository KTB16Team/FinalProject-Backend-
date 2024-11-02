package aimo.backend.domains.privatePost.dto;

import java.time.LocalDate;
import aimo.backend.domains.member.model.Gender;
import jakarta.validation.constraints.NotNull;

public record SummaryAndJudgementRequest(
	@NotNull(message = "제목이 비었습니다.")
	String title,
	@NotNull(message = "대화록이 비었습니다.")
	String script,
	@NotNull(message = "유저명이 비었습니다.")
	String username,
	@NotNull(message = "성별이 비었습니다.")
	Gender gender,
	@NotNull(message = "생년월일이 비었습니다.")
	LocalDate birthdate
) {
}
