package aimo.backend.domains.vote.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.mapper.VoteMapper;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.service.MemberService;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.vote.dto.SaveVotePostRequest;
import aimo.backend.domains.vote.entity.Vote;
import aimo.backend.domains.post.service.PostService;
import aimo.backend.domains.vote.model.Side;
import aimo.backend.domains.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

	private final VoteRepository voteRepository;
	private final MemberService memberService;
	private final PostService postService;

	// 투표하기
	@Transactional(rollbackFor = ApiException.class)
	public void votePost(SaveVotePostRequest saveVotePostRequest) {
		Long postId = saveVotePostRequest.postId();
		Member member = memberService.findBy(saveVotePostRequest.memberId());
		Post post = postService.findById(postId);
		Side side = saveVotePostRequest.side();

		voteRepository.findByPostIdAndMemberId(postId, member.getId())
			.ifPresentOrElse(

				(vote) -> vote.changeSide(side),
				() -> {
					Vote newVote = VoteMapper.toEntity(post, member, side);
					voteRepository.save(newVote);
				}
			);
	}
}
