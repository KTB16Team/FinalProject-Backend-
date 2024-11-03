package aimo.backend.domains.post.dto.response;

import java.time.LocalDateTime;

public record FindPostsByPostTypeResponse(
		Long id,
		String title,
		String content_preview,
		Integer likes_count,
		Integer views_count,
		Integer comments_count,
		Float vote_rate_plaintiff,
		Float vote_rate_defendant,
		LocalDateTime created_at
) {}
