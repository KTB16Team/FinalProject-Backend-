package aimo.backend.domains.comment.dto.request;

import jakarta.validation.constraints.Size;

public record SaveChildCommentRequest(
	@Size(max = 1500, message = "댓글은 최대 500자까지 입력할 수 있습니다.")
	String content
) {

	public static SaveChildCommentRequest of(String content) {
		return new SaveChildCommentRequest(content);
	}
}
