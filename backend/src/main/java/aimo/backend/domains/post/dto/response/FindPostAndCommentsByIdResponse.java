package aimo.backend.domains.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.vote.model.Side;
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime createdAt;
	private final List<ParentCommentDto> comments;

	public static FindPostAndCommentsByIdResponse from(Member member, Post post, List<ParentComment> parentComments, boolean postLikeExists) {
		return new FindPostAndCommentsByIdResponse(
			post.getMember() == member,
			postLikeExists,
			post.getVotes().stream()
				.filter(vote -> vote.getMember() == member)
				.findFirst()
				.map(vote -> vote.getSide().getValue())
				.orElse(Side.NONE.getValue()),
			post.getTitle(),
			post.getMember().getNickname(),
			post.getSummaryAi(),
			post.getPostLikesCount(),
			post.getPostViewsCount(),
			post.getCommentsCount(),
			post.getVotesCount(),
			post.getPlaintiffVotesCount(),
			post.getDefendantVotesCount(),
			post.getCreatedAt(),
			parentComments.stream()
				.map(parentComment -> ParentCommentDto.of(member, parentComment))
				.toList()
		);
	}

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
			boolean isMine = parentComment.getMember() == member;
			boolean isLiked = parentComment.getParentCommentLikes()
				.stream()
				.anyMatch(like -> like.getMember() == member);
			List<ChildCommentDto> childCommentDtos = parentComment.getChildComments()
				.stream()
				.map(childComment -> ChildCommentDto.of(member, childComment))
				.toList();

			return new ParentCommentDto(
				isMine,
				isLiked,
				parentComment.getId(),
				parentComment.getContent(),
				parentComment.getNickname(),
				parentComment.getLikesCount(),
				parentComment.getCreatedAt(),
				childCommentDtos
			);
		}
	}

	@Data
	@AllArgsConstructor
	private static class ChildCommentDto {
		private final Boolean isMine;
		private final Boolean isLiked;
		private final Long childCommentId;
		private final String content;
		private final String nickname;
		private final Integer likesCount;
		private final LocalDateTime createdAt;

		public static ChildCommentDto of(Member member, ChildComment childComment) {
			boolean isMine = childComment.getMember() == member;
			boolean isLiked = childComment.getChildCommentLikes()
				.stream()
				.anyMatch(like -> like.getMember() == member);

			return new ChildCommentDto(
				isMine,
				isLiked,
				childComment.getId(),
				childComment.getContent(),
				childComment.getNickname(),
				childComment.getLikesCount(),
				childComment.getCreatedAt()
			);
		}
	}
}
