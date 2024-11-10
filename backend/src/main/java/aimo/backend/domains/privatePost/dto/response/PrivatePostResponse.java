package aimo.backend.domains.privatePost.dto.response;

import jakarta.validation.constraints.NotNull;

public record PrivatePostResponse(
	Long postId,
	String title,
	String summaryAi,
	String stancePlaintiff,
	String stanceDefendant,
	Integer faultRatePlaintiff,
	Integer faultRateDefendant,
	String judgement,
	Boolean published
) {
}
