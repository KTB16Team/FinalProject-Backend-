package aimo.backend.domains.post.dto.requset;

import java.time.LocalDateTime;

import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.post.entity.Post;

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

	public static FindCommentedPostsByIdRequest from(ParentComment parentComment) {
		Post post = parentComment.getPost();

		final Float plaintiffVotesCount = (float)post.getPlaintiffVotesCount();
		final Float defendantVotesCount = (float)post.getDefendantVotesCount();
		final Float votesCount = (float)post.getVotesCount();

		// 투표율 계산
		float voteRatePlaintiff = 0f;
		float voteRateDefendant = 0f;
		if (votesCount != 0) {
			voteRatePlaintiff = plaintiffVotesCount / votesCount;
			voteRateDefendant = defendantVotesCount / votesCount;
		}

		return new FindCommentedPostsByIdRequest(
			post.getId(),
			post.getTitle(),
			post.getPreview(),
			post.getPostLikesCount(),
			post.getPostViewsCount(),
			post.getCommentsCount(),
			voteRatePlaintiff,
			voteRateDefendant,
			post.getCreatedAt(),
			post.getCreatedAt()
		);
	}
}
