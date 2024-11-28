package aimo.backend.domains.comment.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class FindCommentsResponse {

	private final Boolean isMine;
	private final Boolean isLiked;
	private final Long commentId;
	private final String content;
	private final String nickname;
	private final Integer likesCount;
	private final LocalDateTime createdAt;
	private final List<ChildCommentDto> childComments;

	public static FindCommentsResponse of(Member member, ParentComment parentComment) {
		boolean isMine = parentComment.getMember() == member;
		boolean isLiked = parentComment.getParentCommentLikes()
			.stream()
			.anyMatch(like -> like.getMember() == member);
		List<ChildCommentDto> childCommentDtos = parentComment.getChildComments()
			.stream()
			.map(childComment -> ChildCommentDto.of(member, childComment))
			.toList();

		return new FindCommentsResponse(
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
