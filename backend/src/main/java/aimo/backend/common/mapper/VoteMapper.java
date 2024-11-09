package aimo.backend.common.mapper;

import org.springframework.stereotype.Component;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
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
}
