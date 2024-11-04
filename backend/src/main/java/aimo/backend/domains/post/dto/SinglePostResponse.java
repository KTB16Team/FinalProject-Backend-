package aimo.backend.domains.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import aimo.backend.domains.comment.dto.response.CommentResponse;

public record SinglePostResponse(
	String title,
	String nickname,
	String content,
	@JsonProperty("votes_plaintiff")
	Long votesPlaintiff,
	@JsonProperty("votes_defendant")
	Long votesDefendant,
	Long likes,
	@JsonProperty("views_count")
	Long viewsCount,
	@JsonProperty("votes_count")
	Long votesCount,
	@JsonProperty("comments_count")
	Long commentsCount,
	@JsonProperty("comments")
	List<CommentResponse> comments,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdAt) {
}
