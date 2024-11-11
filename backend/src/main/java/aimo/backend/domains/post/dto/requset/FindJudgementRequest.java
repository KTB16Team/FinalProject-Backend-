package aimo.backend.domains.post.dto.requset;

public record FindJudgementRequest(Long postId) {

	public static FindJudgementRequest of(Long postId) {
		return new FindJudgementRequest(postId);
	}
}
