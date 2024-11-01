package aimo.backend.domains.privatePost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.validation.constraints.NotNull;

public record SummaryAndJudgementResponse(
	String title,
	String stancePlaintiff,
	String stanceDefendant,
	String summaryAi,
	String judgement,
	Double faultRate,
	OriginType originType
) {
}
