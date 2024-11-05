package aimo.backend.domains.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import aimo.backend.domains.comment.dto.response.CommentResponse;

public record SinglePostResponse(
	String title,
	String nickname,
	String content,
	Long votesPlaintiff,
	Long votesDefendant,
	Long likesCount,
	Long viewsCount,
	Long votesCount,
	Long commentsCount,
	List<CommentResponse> comments,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdAt) {
}
