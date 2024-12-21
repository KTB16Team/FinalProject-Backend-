package aimo.backend.domains.ai.dto.parameter;

import aimo.backend.domains.privatePost.model.OriginType;

public record JudgementToAiParameter(
	Long memberId,
	String content,
	OriginType originType
) {

	public static JudgementToAiParameter of(
		Long memberId,
		String content,
		OriginType originType
	) {
		return new JudgementToAiParameter(
			memberId,
			content,
			originType
		);
	}
}
