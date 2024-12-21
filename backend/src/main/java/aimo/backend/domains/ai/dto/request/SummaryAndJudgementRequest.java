package aimo.backend.domains.ai.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.Gender;

public record SummaryAndJudgementRequest(
	Long id,
	String content,
	String nickname,
	Gender gender,
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate birth
) {

	public static SummaryAndJudgementRequest from(
		Long id,
		String content,
		Member member
	) {
		return new SummaryAndJudgementRequest(
			id,
			content,
			member.getNickname(),
			member.getGender(),
			member.getBirthDate()
		);
	}
}
