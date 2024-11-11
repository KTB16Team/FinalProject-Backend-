package aimo.backend.domains.privatePost.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.Gender;
import aimo.backend.domains.privatePost.dto.parameter.JudgementToAiParameter;
import jakarta.validation.constraints.NotNull;

public record SummaryAndJudgementRequest(
	@NotNull(message = "대화록이 비었습니다.")
	String content,
	@NotNull(message = "유저명이 비었습니다.")
	String nickname,
	@NotNull(message = "성별이 비었습니다.")
	Gender gender,
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "생년월일이 비었습니다.")
	LocalDate birth
) {

	public static SummaryAndJudgementRequest of(
		String content,
		String nickname,
		Gender gender,
		LocalDate birth
	) {
		return new SummaryAndJudgementRequest(content, nickname, gender, birth);
	}

	public static SummaryAndJudgementRequest from(
		JudgementToAiParameter 	parameter,
		Member member
	) {
		return new SummaryAndJudgementRequest(
			parameter.content(),
			member.getNickname(),
			member.getGender(),
			member.getBirthDate()
		);
	}
}
