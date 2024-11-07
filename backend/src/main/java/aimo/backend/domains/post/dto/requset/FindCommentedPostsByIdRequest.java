package aimo.backend.domains.post.dto.requset;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record FindCommentedPostsByIdRequest(
	Long id,
	String title,
	String contentPreview,
	Integer likesCount,
	Integer viewsCount,
	Integer commentsCount,
	Float voteRatePlaintiff,
	Float voteRateDefendant,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdAt,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime commentedAt
) {
}
