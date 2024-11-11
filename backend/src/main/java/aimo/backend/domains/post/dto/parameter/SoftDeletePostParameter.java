package aimo.backend.domains.post.dto.parameter;

public record SoftDeletePostParameter(
	Long postId
) {
	public static SoftDeletePostParameter of(Long postId) {
		return new SoftDeletePostParameter(postId);
	}
}
