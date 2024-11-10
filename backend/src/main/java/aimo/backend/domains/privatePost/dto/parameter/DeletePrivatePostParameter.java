package aimo.backend.domains.privatePost.dto.parameter;

public record DeletePrivatePostParameter(
	Long memberId,
	Long privatePostId
) {
}
