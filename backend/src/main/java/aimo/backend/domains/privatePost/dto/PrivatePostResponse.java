package aimo.backend.domains.privatePost.dto;

public record PrivatePostResponse(
	String title,
	String summaryAi,
	String stancePlaintiff,
	String stanceDefendant,
	String judgement,
	Double faultRate,
	Boolean published
) {
}
