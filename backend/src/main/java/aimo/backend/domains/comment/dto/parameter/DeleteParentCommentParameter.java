package aimo.backend.domains.comment.dto.parameter;

public record DeleteParentCommentParameter(
	Long memberId,
	Long parentCommentId
) {
}
