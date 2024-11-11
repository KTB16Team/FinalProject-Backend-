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
) {

	public static FindPostsByPostTypeResponse of(
		Long id,
		String title,
		String contentPreview,
		Integer likesCount,
		Integer viewsCount,
		Integer commentsCount,
		Float voteRatePlaintiff,
		Float voteRateDefendant,
		LocalDateTime createdAt
	) {
		return new FindPostsByPostTypeResponse(
			id,
			title,
			contentPreview,
			likesCount,
			viewsCount,
			commentsCount,
			voteRatePlaintiff,
			voteRateDefendant,
			createdAt
		);
	}
}
