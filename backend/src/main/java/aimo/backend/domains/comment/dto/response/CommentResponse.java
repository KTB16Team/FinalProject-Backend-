package aimo.backend.domains.comment.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record CommentResponse(
	Long commentId,
	String username,
	String content,
	Long likes,
	List<ChildCommentResponse>childComments,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdAt
) {
}
