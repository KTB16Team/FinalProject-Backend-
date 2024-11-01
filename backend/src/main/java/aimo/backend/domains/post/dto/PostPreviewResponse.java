package aimo.backend.domains.post.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PostPreviewResponse(
	@JsonProperty("post_id")
	Long postId,
	@JsonProperty("title")
	String title,
	@JsonProperty("content_preview")
	String contentPreview,
	@JsonProperty("likes_count")
	Integer likesCount,
	@JsonProperty("views_count")
	Integer viewsCount,
	@JsonProperty("comments_count")
	Integer commentsCount,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdAt
) {
}
