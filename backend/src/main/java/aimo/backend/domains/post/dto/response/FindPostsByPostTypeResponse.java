package aimo.backend.domains.post.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import aimo.backend.domains.post.dto.requset.FindCommentedPostsByIdRequest;
import aimo.backend.domains.post.entity.Post;

public record FindPostsByPostTypeResponse(
	Long id,
	String title,
	String contentPreview,
	Integer likesCount,
	Integer viewsCount,
	Integer commentsCount,
	Float voteRatePlaintiff,
	Float voteRateDefendant,

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdAt
) {

	public static FindPostsByPostTypeResponse from(FindCommentedPostsByIdRequest request) {
		return new FindPostsByPostTypeResponse(
			request.postId(),
			request.title(),
			request.contentPreview(),
			request.likesCount(),
			request.viewsCount(),
			request.commentsCount(),
			request.voteRatePlaintiff(),
			request.voteRateDefendant(),
			request.createdAt()
		);
	}

	public static FindPostsByPostTypeResponse from(Post post) {
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

		return new FindPostsByPostTypeResponse(
			post.getId(),
			post.getTitle(),
			post.getPreview(),
			post.getPostLikesCount(),
			post.getPostViewsCount(),
			post.getCommentsCount(),
			voteRatePlaintiff,
			voteRateDefendant,
			post.getCreatedAt()
		);
	}
}
