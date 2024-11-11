package aimo.backend.domains.comment.dto.parameter;

public record ValidAndUpdateChildCommentParameter(
	Long memberId,
	Long childCommentId,
	String content
) {

	public static ValidAndUpdateChildCommentParameter of(Long memberId, Long childCommentId, String content) {
		return new ValidAndUpdateChildCommentParameter(memberId, childCommentId, content);
	}
}
