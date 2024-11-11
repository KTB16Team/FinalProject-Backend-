package aimo.backend.domains.comment.dto.parameter;

public record UpdateParentCommentParameter(
	Long memberId,
	Long parentCommentId,
	String content
) {

	public static UpdateParentCommentParameter of(Long memberId, Long parentCommentId, String content) {
		return new UpdateParentCommentParameter(memberId, parentCommentId, content);
	}
}
