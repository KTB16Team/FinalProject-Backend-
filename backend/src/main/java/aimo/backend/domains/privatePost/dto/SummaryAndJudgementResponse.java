package aimo.backend.domains.privatePost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.validation.constraints.NotNull;

public record SummaryAndJudgementResponse(
	@NotNull(message = "Title is required")
	String title,
	@NotNull(message = "Stance of plaintiff is required")
	@JsonProperty("stance_plaintiff")
	String stancePlaintiff,
	@NotNull(message = "Stance of defendant is required")
	@JsonProperty("stance_defendant")
	String stanceDefendant,
	@NotNull(message = "Summary of AI is required")
	@JsonProperty("summary_ai")
	String summaryAi,
	@NotNull(message = "Judgement is required")
	String judgement,
	@NotNull(message = "Origin type is required")
	@JsonProperty("fault_rate")
	Double faultRate,
	@NotNull(message = "Origin type is required")
	@JsonProperty("origin_type")
	OriginType originType
) {
}
