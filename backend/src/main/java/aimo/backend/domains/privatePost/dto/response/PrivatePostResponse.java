package aimo.backend.domains.privatePost.dto.response;

import jakarta.validation.constraints.NotNull;

public record PrivatePostResponse(
	Long privatePostId,
	String title,
	String summaryAi,
	String stancePlaintiff,
	String stanceDefendant,
	String judgement,
	Integer faultRatePlaintiff,
	Integer faultRateDefendant,
	Boolean published
) {
}
