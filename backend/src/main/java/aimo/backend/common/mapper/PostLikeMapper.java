package aimo.backend.common.mapper;

import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.like.dto.parameter.LikePostParameter;
import aimo.backend.domains.like.entity.PostLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;

public class PostLikeMapper {
	public static PostLike toEntity(Member member, Post post) {
		return PostLike
			.builder()
			.member(member)
			.post(post)
			.build();
	}

	public static LikePostParameter toLikePostParameter(Long postId, LikeType likeType) {
		return new LikePostParameter(MemberLoader.getMemberId(), postId, likeType);
	}
}
