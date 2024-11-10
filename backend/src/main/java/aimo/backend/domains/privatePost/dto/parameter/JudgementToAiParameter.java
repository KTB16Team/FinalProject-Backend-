package aimo.backend.domains.privatePost.dto.parameter;

import aimo.backend.domains.privatePost.model.OriginType;

public record JudgementToAiParameter(
	Long memberId,
	String content,
	OriginType originType
) {
}
