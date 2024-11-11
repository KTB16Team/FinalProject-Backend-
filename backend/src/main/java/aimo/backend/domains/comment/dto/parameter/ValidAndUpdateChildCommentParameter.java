package aimo.backend.domains.comment.dto.parameter;

public record ValidAndUpdateChildCommentParameter(
	Long memberId,
	Long childCommentId,
	String content
) {
}
