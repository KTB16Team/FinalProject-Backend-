package aimo.backend.domains.comment.dto.request;

public record ValidAndUpdateChildCommentParameter(
	Long memberId,
	Long childCommentId,
	String content
) {
}
