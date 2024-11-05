package aimo.backend.domains.privatePost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.validation.constraints.NotNull;

public record SummaryAndJudgementResponse(
	@NotNull(message = "제목이 비었습니다.")
	String title,
	@NotNull(message = "원고가 비었습니다.")
	String stancePlaintiff,
	@NotNull(message = "피고가 비었습니다.")
	String stanceDefendant,
	@NotNull(message = "요약이 비었습니다.")
	String summaryAi,
	@NotNull(message = "판결문이 비었습니다.")
	String judgement,
	@NotNull(message = "과실 비율이 비었습니다.")
	Double faultRate,
	@NotNull(message = "원본 타입이 비었습니다.")
	OriginType originType
) { }
