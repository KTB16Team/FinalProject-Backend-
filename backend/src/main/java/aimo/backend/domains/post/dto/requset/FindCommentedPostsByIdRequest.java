package aimo.backend.domains.post.dto.requset;

import java.time.LocalDateTime;

public record FindCommentedPostsByIdRequest(
	Long postId,
	String title,
	String contentPreview,
	Integer likesCount,
	Integer viewsCount,
	Integer commentsCount,
	Float voteRatePlaintiff,
	Float voteRateDefendant,
	LocalDateTime createdAt,
	LocalDateTime commentedAt
) {

	public static FindCommentedPostsByIdRequest of(
		Long postId,
		String title,
		String contentPreview,
		Integer likesCount,
		Integer viewsCount,
		Integer commentsCount,
		Float voteRatePlaintiff,
		Float voteRateDefendant,
		LocalDateTime createdAt,
		LocalDateTime commentedAt
	) {
		return new FindCommentedPostsByIdRequest(
			postId,
			title,
			contentPreview,
			likesCount,
			viewsCount,
			commentsCount,
			voteRatePlaintiff,
			voteRateDefendant,
			createdAt,
			commentedAt
		);
	}
}
