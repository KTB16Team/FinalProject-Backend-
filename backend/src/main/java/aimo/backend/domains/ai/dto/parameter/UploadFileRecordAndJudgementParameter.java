package aimo.backend.domains.ai.dto.parameter;

import aimo.backend.domains.privatePost.model.OriginType;

public record UploadFileRecordAndJudgementParameter(
	Long memberId,
	String content,
	OriginType originType,
	Long privatePostId
) {
}
