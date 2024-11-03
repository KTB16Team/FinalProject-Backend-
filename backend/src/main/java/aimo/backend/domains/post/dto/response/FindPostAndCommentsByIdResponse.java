package aimo.backend.domains.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindPostAndCommentsByIdResponse {
	private final Boolean is_mine;
	private final String title;
	private final String username;
	private final String content;
	private final Integer likes_count;
	private final Integer views_count;
	private final Integer comments_count;
	private final Integer votes_count;
	private final Integer votes_plaintiff;
	private final Integer votes_defendant;
	private final LocalDateTime created_at;
	private final List<ParentCommentDto> comments;

	@Data
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ParentCommentDto {
		private final Boolean is_mine;
		private final Long comment_id;
		private final String content;
		private final String username;
		private final Integer likes_count;
		private final LocalDateTime created_at;
		private final List<ChildCommentDto> child_comments;

		public static ParentCommentDto of(Member member, ParentComment parentComment) {
			return new ParentCommentDto(
				parentComment.getMember() == member,
				parentComment.getId(),
				parentComment.getContent(),
				parentComment.getMemberName(),
				parentComment.getLikesCount(),
				parentComment.getCreatedAt(),
				parentComment.getChildComments().stream()
					.map(childComment -> new ChildCommentDto(
						parentComment.getMember() == member,
						childComment.getId(),
						childComment.getContent(),
						childComment.getMemberName(),
						childComment.getLikesCount(),
						childComment.getCreatedAt()
					))
					.toList()
			);
		}
	}

	@Data
	@AllArgsConstructor
	private static class ChildCommentDto {
		private final Boolean is_mine;
		private final Long child_comment_id;
		private final String content;
		private final String username;
		private final Integer likes_count;
		private final LocalDateTime created_at;
	}
}
