package aimo.backend.domains.comment.dto.parameter;

public record SaveChildCommentParameter(
	Long memberId,
	Long postId,
	Long parentCommentId,
	String content
) {

	public static SaveChildCommentParameter of(Long memberId, Long postId, Long parentCommentId, String content) {
		return new SaveChildCommentParameter(memberId, postId, parentCommentId, content);
	}
}
