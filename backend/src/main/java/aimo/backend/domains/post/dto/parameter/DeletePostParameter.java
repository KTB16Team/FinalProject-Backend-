package aimo.backend.domains.post.dto.parameter;

public record DeletePostParameter(
	Long memberId,
	Long postId
) {

	public static DeletePostParameter of(Long memberId, Long postId) {
		return new DeletePostParameter(memberId, postId);
	}
}
