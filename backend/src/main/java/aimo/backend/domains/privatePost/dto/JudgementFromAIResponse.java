package aimo.backend.domains.privatePost.dto;

public record JudgementFromAIResponse(
	String title,
	String stancePlaintiff,
	String stanceDefendant,
	String stanceAi,
	String judgement,
	String faultRate
) {}
