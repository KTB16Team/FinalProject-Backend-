package aimo.backend.domains.privatePost.dto.parameter;

import aimo.backend.domains.ai.dto.request.UpdateContentToPrivatePostRequest;

public record UpdateContentToPrivatePostParameter(
	String accessKey,
	Boolean status,
	Long privatePostId,
	String title,
	String stancePlaintiff,
	String stanceDefendant,
	String summaryAi,
	String judgement,
	Float faultRate
) {
	public static UpdateContentToPrivatePostParameter from(UpdateContentToPrivatePostRequest request) {
		return new UpdateContentToPrivatePostParameter(
			request.accessKey(),
			request.status(),
			request.privatePostId(),
			request.title(),
			request.stancePlaintiff(),
			request.stanceDefendant(),
			request.summaryAi(),
			request.judgement(),
			request.faultRate()
		);
	}
}

