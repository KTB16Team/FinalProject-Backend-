package aimo.backend.domains.comment.dto.request;

public record ValidAndDeleteParentCommentParameter(
	Long memberId,
	Long childCommentId
) {
}
