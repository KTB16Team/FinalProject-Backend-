package aimo.backend.domains.view.dto;

public record IncreasePostViewParameter(
	Long memberId,
	Long postId
) {

	public static IncreasePostViewParameter of(Long memberId, Long postId) {
		return new IncreasePostViewParameter(memberId, postId);
	}
}
