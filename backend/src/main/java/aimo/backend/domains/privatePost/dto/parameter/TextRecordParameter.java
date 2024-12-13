package aimo.backend.domains.privatePost.dto.parameter;

import aimo.backend.domains.privatePost.dto.request.UploadTextRecordAndRequestJudgementRequest;

public record TextRecordParameter(
	String content
) {

	public static TextRecordParameter from(UploadTextRecordAndRequestJudgementRequest request) {
		return new TextRecordParameter(request.content());
	}
}
