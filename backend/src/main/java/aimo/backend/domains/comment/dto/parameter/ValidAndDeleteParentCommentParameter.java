package aimo.backend.domains.comment.dto.parameter;

public record ValidAndDeleteParentCommentParameter(
	Long memberId,
	Long childCommentId
) {
}
