package aimo.backend.domains.privatePost.dto.request;

import jakarta.validation.constraints.NotNull;

public record UploadTextRecordAndRequestJudgementRequest(
	@NotNull(message = "내용을 입력하세요.")
	String content
) {

	public static UploadTextRecordAndRequestJudgementRequest of(String content) {
		return new UploadTextRecordAndRequestJudgementRequest(content);
	}
}
