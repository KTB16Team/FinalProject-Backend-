package aimo.backend.common.mapper;

import org.springframework.stereotype.Component;

import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.vote.dto.SaveVotePostParameter;
import aimo.backend.domains.vote.entity.Vote;
import aimo.backend.domains.vote.model.Side;
import lombok.RequiredArgsConstructor;

public class VoteMapper {

	public static Vote toEntity(Post post, Member member, Side side) {
		return Vote.builder()
			.post(post)
			.member(member)
			.side(side)
			.build();
	}

	public static SaveVotePostParameter toSavePostParameter(Long postId, Side side) {
		Long memberId = MemberLoader.getMemberId();
		return new SaveVotePostParameter(memberId, postId, side);
	}
}
