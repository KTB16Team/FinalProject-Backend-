package aimo.backend.domains.privatePost.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

public record SummaryAndJudgementRequest(
	@NotNull(message = "대화록이 비었습니다.")
	String content,
	@NotNull(message = "유저명이 비었습니다.")
	String nickname,
	@NotNull(message = "성별이 비었습니다.")
	String gender,
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "생년월일이 비었습니다.")
	LocalDate birth
	
) {
}
