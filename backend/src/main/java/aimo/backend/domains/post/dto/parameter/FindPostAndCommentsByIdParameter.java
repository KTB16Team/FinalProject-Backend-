package aimo.backend.domains.post.dto.parameter;

public record FindPostAndCommentsByIdParameter(
	Long postId,
	Long memberId) {
}
