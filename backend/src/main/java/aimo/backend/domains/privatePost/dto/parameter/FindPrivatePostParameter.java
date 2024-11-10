package aimo.backend.domains.privatePost.dto.parameter;

public record FindPrivatePostParameter(
	Long memberId,
	Long privatePostId
) {
}
