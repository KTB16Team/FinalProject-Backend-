package aimo.backend.domains.privatePost.dto.parameter;

import aimo.backend.domains.privatePost.dto.request.JudgementToAiRequest;
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

	public static JudgementToAiParameter from(Long memberId, JudgementToAiRequest request) {
		return new JudgementToAiParameter(
			memberId,
			request.content(),
			request.originType()
		);
	}
}
