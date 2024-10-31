package aimo.backend.domains.privatePost.dto;

import java.time.LocalDate;
import aimo.backend.domains.member.model.Gender;

public record SummaryAndJudgementRequest(
	String title,
	String script,
	String username,
	Gender gender,
	LocalDate birthdate
) {
}
