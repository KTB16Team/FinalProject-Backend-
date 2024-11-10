package aimo.backend.domains.comment.dto.parameter;

public record SaveParentCommentParameter(
	Long memberId,
	Long postId,
	String content
) {
}
