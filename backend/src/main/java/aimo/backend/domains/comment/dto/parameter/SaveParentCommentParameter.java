package aimo.backend.domains.comment.dto.parameter;

public record SaveParentCommentParameter(
	Long memberId,
	Long postId,
	String content
) {

	public static SaveParentCommentParameter of(Long memberId, Long postId, String content) {
		return new SaveParentCommentParameter(memberId, postId, content);
	}
}
