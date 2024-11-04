package aimo.backend.domains.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.entity.PostLike;
import aimo.backend.domains.post.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

	private final PostLikeRepository postLikeRepository;
	private final PostService postService;

	@Transactional(rollbackFor = Exception.class)
	public void like(Member member, Long postId) {
		Post post = postService.findById(postId);

		postLikeRepository.findByMember_IdAndPost_Id(member.getId(), postId)
			.ifPresentOrElse(
				postLikeRepository::delete,
				() -> postLikeRepository.save(new PostLike(post, member))
			);
	}
}
