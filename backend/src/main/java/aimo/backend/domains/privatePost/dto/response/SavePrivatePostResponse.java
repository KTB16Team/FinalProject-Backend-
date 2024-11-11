package aimo.backend.domains.privatePost.dto.response;

public record SavePrivatePostResponse(Long privatePostId) {

	public static SavePrivatePostResponse of(Long privatePostId) {
		return new SavePrivatePostResponse(privatePostId);
	}
}
