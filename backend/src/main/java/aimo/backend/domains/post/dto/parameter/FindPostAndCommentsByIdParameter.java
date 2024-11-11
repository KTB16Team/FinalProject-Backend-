package aimo.backend.domains.post.dto.parameter;

public record FindPostAndCommentsByIdParameter(
	Long postId,
	Long memberId
) {

	public static FindPostAndCommentsByIdParameter of(Long postId, Long memberId) {
		return new FindPostAndCommentsByIdParameter(postId, memberId);
	}
}
