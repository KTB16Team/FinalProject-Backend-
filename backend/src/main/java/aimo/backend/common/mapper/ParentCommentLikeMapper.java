package aimo.backend.common.mapper;

import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.like.dto.LikeParentCommentRequest;
import aimo.backend.domains.like.entity.ParentCommentLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.member.entity.Member;

public class ParentCommentLikeMapper {

	public static ParentCommentLike toEntity(Member member, ParentComment parentComment) {
		return ParentCommentLike.builder().parentComment(parentComment).member(member).build();
	}

	public static LikeParentCommentRequest toLikeParentCommentRequest(
		Long memberId,
		Long parentCommentId,
		LikeType likeType) {
		return new LikeParentCommentRequest(memberId, parentCommentId, likeType);
	}
}
