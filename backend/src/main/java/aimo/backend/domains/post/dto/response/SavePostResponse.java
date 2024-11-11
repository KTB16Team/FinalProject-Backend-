package aimo.backend.domains.post.dto.response;

public record SavePostResponse(Long postId) {

	public static SavePostResponse of(Long postId) {
		return new SavePostResponse(postId);
	}
}
