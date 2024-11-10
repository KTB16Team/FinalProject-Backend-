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
}
