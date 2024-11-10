package aimo.backend.domains.comment.dto.parameter;

public record UpdateParentCommentParameter(
	Long memberId,
	Long parentCommentId,
	String content
) {
}
