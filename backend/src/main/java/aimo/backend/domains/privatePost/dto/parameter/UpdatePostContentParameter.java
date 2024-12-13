package aimo.backend.domains.privatePost.dto.parameter;

public record UpdatePostContentParameter(
	String stancePlaintiff,
	String stanceDefendant,
	String title,
	String summaryAi,
	String judgement,
	Integer faultRatePlaintiff,
	Integer faultRateDefendant
) {
}
