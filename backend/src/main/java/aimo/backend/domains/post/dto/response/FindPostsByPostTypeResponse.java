package aimo.backend.domains.post.dto.response;

import java.time.LocalDateTime;

public record FindPostsByPostTypeResponse(
		Long id,
		String title,
		String contentPreview,
		Integer likesCount,
		Integer viewsCount,
		Integer commentsCount,
		Float voteRatePlaintiff,
		Float voteRateDefendant,
		LocalDateTime createdAt
) {}
