package aimo.backend.domains.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.entity.Vote;
import aimo.backend.domains.post.model.Side;
import aimo.backend.domains.post.repository.VoteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

	private final VoteRepository voteRepository;
	private final PostService postService;

	// 투표하기
	@Transactional(rollbackFor = ApiException.class)
	public void votePost(Member member, Long postId, Side side) {
		Post post = postService.findById(postId);

		voteRepository.findByPost_IdAndMember_Id(postId, member.getId())
			.ifPresentOrElse(
				(vote) -> vote.changeSide(side),
				() -> voteRepository.save(new Vote(post, member, side))
			);
	}
}
