package aimo.backend.domains.post.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record FindPostsByPostTypeResponse(
		Long id,
		String title,
		String contentPreview,
		Integer likesCount,
		Integer viewsCount,
		Integer commentsCount,
		Float voteRatePlaintiff,
		Float voteRateDefendant,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		LocalDateTime createdAt
) {}
