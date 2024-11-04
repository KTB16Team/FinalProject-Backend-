package aimo.backend.domains.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.like.entity.PostLike;
import aimo.backend.domains.like.repository.PostLikeRepository;
import aimo.backend.domains.post.service.PostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

	private final PostLikeRepository postLikeRepository;
	private final PostService postService;

	@Transactional(rollbackFor = Exception.class)
	public void likePost(Member member, Long postId, LikeType likeType) {
		Post post = postService.findById(postId);

		if (likeType == LikeType.LIKE) {
			// 라이크가 이미 존재하면 무시
			if (postLikeRepository.existsByPost_IdAndMember_Id(postId, member.getId())) {
				return;
			}

			postLikeRepository.save(PostLike.builder()
				.post(post)
				.member(member)
				.build());
		} else {
			postLikeRepository.deleteByMember_IdAndPost_Id(member.getId(), postId);
		}
	}
}
