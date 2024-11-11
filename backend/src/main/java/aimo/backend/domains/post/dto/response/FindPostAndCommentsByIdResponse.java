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

	private final Boolean isMine;
	private final Boolean isLiked;
	private final String side;
	private final String title;
	private final String nickname;
	private final String content;
	private final Integer likesCount;
	private final Integer viewsCount;
	private final Integer commentsCount;
	private final Integer votesCount;
	private final Integer votesPlaintiff;
	private final Integer votesDefendant;
	private final LocalDateTime createdAt;
	private final List<ParentCommentDto> comments;

	@Data
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ParentCommentDto {
		private final Boolean isMine;
		private final Boolean isLiked;
		private final Long commentId;
		private final String content;
		private final String nickname;
		private final Integer likesCount;
		private final LocalDateTime createdAt;
		private final List<ChildCommentDto> childComments;

		public static ParentCommentDto of(Member member, ParentComment parentComment) {
			return new ParentCommentDto(
				parentComment.getMember() == member,
				parentComment.getParentCommentLikes().stream()
					.anyMatch(like -> like.getMember() == member),
				parentComment.getId(),
				parentComment.getContent(),
				parentComment.getNickname(),
				parentComment.getLikesCount(),
				parentComment.getCreatedAt(),
				parentComment.getChildComments().stream()
					.map(childComment -> new ChildCommentDto(
						childComment.getMember() == member,
						childComment.getChildCommentLikes().stream()
							.anyMatch(like -> like.getMember() == member),
						childComment.getId(),
						childComment.getContent(),
						childComment.getNickname(),
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
		private final Boolean isMine;
		private final Boolean isLiked;
		private final Long childCommentDd;
		private final String content;
		private final String nickname;
		private final Integer likesCount;
		private final LocalDateTime createdAt;

		public static ChildCommentDto of(Member member, ParentComment parentComment) {
			return new ChildCommentDto(
				parentComment.getMember() == member,
				parentComment.getParentCommentLikes().stream()
					.anyMatch(like -> like.getMember() == member),
				parentComment.getId(),
				parentComment.getContent(),
				parentComment.getNickname(),
				parentComment.getLikesCount(),
				parentComment.getCreatedAt()
			);
		}
	}
}
