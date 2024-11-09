package aimo.backend.domains.comment.dto.request;

public record SaveChildCommentParameter(
	Long memberId,
	Long postId,
	Long parentCommentId,
	String content
) {
}
