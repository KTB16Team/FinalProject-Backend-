package aimo.backend.domains.post.dto.parameter;

public record SoftDeletePostParameter(
	Long postId,
	Long memberId
) {
	public static SoftDeletePostParameter of(Long postId, Long memberId) {
		return new SoftDeletePostParameter(postId, memberId);
	}
}
