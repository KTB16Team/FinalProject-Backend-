package aimo.backend.domains.privatePost.dto.request;

public record DeletePrivatePostRequest(Long privatePostId) {

	public static DeletePrivatePostRequest of(Long privatePostId) {
		return new DeletePrivatePostRequest(privatePostId);
	}
}
