package aimo.backend.common.mapper;

import org.springframework.stereotype.Component;

import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.like.dto.parameter.LikeChildCommentParameter;
import aimo.backend.domains.like.entity.ChildCommentLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChildCommentLikeMapper {

	public static ChildCommentLike toChildCommentLike(Member member, ChildComment childComment) {
		return ChildCommentLike.builder().childComment(childComment).member(member).build();
	}

	public static LikeChildCommentParameter toLikeChildCommentParameter(
		Long childCommentId,
		LikeType likeType) {
		Long memberId = MemberLoader.getMemberId();
		return new LikeChildCommentParameter(memberId, childCommentId, likeType);
	}
}
