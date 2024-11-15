package aimo.backend.domains.vote.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.repository.PostRepository;
import aimo.backend.domains.vote.dto.SaveVotePostParameter;
import aimo.backend.domains.vote.entity.Vote;
import aimo.backend.domains.vote.model.Side;
import aimo.backend.domains.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

	private final VoteRepository voteRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final ApplicationEventPublisher eventPublisher;

	// 투표하기
	@Transactional(rollbackFor = ApiException.class)
	public void votePost(SaveVotePostParameter parameter) {
		Long postId = parameter.postId();
		Side side = parameter.side();

		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));

		voteRepository.findByPostIdAndMemberId(postId, member.getId())
			.ifPresentOrElse(
				(vote) -> vote.changeSide(side),
				() -> {
					Vote newVote = Vote.from(post, member, side);
					voteRepository.save(newVote);
				}
			);
	}
}
