package aimo.backend.domains.comment.dto.parameter;

public record ValidAndDeleteParentCommentParameter(
	Long memberId,
	Long childCommentId
) {

	public static ValidAndDeleteParentCommentParameter of(Long memberId, Long childCommentId) {
		return new ValidAndDeleteParentCommentParameter(memberId, childCommentId);
	}
}
