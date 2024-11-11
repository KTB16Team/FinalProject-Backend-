package aimo.backend.common.mapper;

import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.like.dto.parameter.LikeParentCommentParameter;
import aimo.backend.domains.like.entity.ParentCommentLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.member.entity.Member;

public class ParentCommentLikeMapper {

	public static ParentCommentLike toEntity(Member member, ParentComment parentComment) {
		return ParentCommentLike.builder().parentComment(parentComment).member(member).build();
	}

	public static LikeParentCommentParameter toLikeParentCommentParameter(
		Long parentCommentId,
		LikeType likeType) {
		Long memberId = MemberLoader.getMemberId();
		return new LikeParentCommentParameter(memberId, parentCommentId, likeType);
	}
}
