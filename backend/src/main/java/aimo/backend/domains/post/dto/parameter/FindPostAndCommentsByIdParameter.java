package aimo.backend.domains.post.dto.parameter;

public record FindPostAndCommentsByIdParameter(
	Long memberId,
	Long postId
) {

	public static FindPostAndCommentsByIdParameter of(Long memberId, Long postId) {
		return new FindPostAndCommentsByIdParameter(memberId, postId);
	}
}
