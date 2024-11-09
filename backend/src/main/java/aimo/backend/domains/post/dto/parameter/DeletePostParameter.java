package aimo.backend.domains.post.dto.parameter;

public record DeletePostParameter(
	Long memberId,
	Long postId
) {
}
