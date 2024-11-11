package aimo.backend.domains.comment.dto.parameter;

public record DeleteParentCommentParameter(
	Long memberId,
	Long parentCommentId
) {

	public static DeleteParentCommentParameter of(Long memberId, Long parentCommentId) {
		return new DeleteParentCommentParameter(memberId, parentCommentId);
	}
}
