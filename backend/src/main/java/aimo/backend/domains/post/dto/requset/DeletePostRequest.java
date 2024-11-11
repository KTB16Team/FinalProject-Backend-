package aimo.backend.domains.post.dto.requset;

public record DeletePostRequest(Long postId) {

	public static DeletePostRequest of(Long postId) {
		return new DeletePostRequest(postId);
	}
}
