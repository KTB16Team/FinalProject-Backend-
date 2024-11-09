package aimo.backend.common.mapper;

import org.springframework.stereotype.Component;

import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.like.dto.LikeChildCommentRequest;
import aimo.backend.domains.like.entity.ChildCommentLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChildCommentLikeMapper {

	public static ChildCommentLike toChildCommentLike(Member member, ChildComment childComment) {
		return ChildCommentLike.builder().childComment(childComment).member(member).build();
	}

	public static LikeChildCommentRequest toLikeChildCommentRequest(
		Long memberId,
		Long childCommentId,
		LikeType likeType) {
		return new LikeChildCommentRequest(memberId, childCommentId, likeType);
	}
}
