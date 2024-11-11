package aimo.backend.domains.privatePost.dto.request;

public record FindPrivatePostRequest(Long privatePostId) {

	public static FindPrivatePostRequest of(Long privatePostId) {
		return new FindPrivatePostRequest(privatePostId);
	}
}
