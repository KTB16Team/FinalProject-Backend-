package aimo.backend.domains.comment.dto.parameter;

public record SaveChildCommentParameter(
	Long memberId,
	Long postId,
	Long parentCommentId,
	String content
) {
}
