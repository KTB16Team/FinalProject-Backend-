package aimo.backend.domains.ai.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateContentToPrivatePostRequest(
	@NotNull(message = "accessKey가 필요합니다.")
	String accessKey,
	@NotNull(message = "status가 필요합니다.")
	Boolean status,
	@NotNull(message = "privatePostId가 필요합니다.")
	Long privatePostId,
	String title,
	String stancePlaintiff,
	String stanceDefendant,
	String summaryAi,
	String judgement,
	Float faultRate
) {
}
