package aimo.backend.domains.post.dto.requset;

public record FindPostAndCommentsRequest(Long postId) {

	public static FindPostAndCommentsRequest of(Long postId) {
		return new FindPostAndCommentsRequest(postId);
	}
}
