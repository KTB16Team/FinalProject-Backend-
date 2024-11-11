package aimo.backend.common.mapper;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.vote.dto.SaveVotePostParameter;
import aimo.backend.domains.vote.entity.Vote;
import aimo.backend.domains.vote.model.Side;

public class VoteMapper {

	public static Vote toEntity(Post post, Member member, Side side) {
		return Vote.builder()
			.post(post)
			.member(member)
			.side(side)
			.build();
	}

	public static SaveVotePostParameter toSavePostParameter(Long memberId, Long postId, Side side) {
		return new SaveVotePostParameter(memberId, postId, side);
	}
}
