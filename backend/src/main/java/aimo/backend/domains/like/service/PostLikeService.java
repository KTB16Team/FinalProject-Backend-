package aimo.backend.domains.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.mapper.PostLikeMapper;
import aimo.backend.domains.like.dto.parameter.LikePostParameter;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.like.entity.PostLike;
import aimo.backend.domains.like.repository.PostLikeRepository;
import aimo.backend.domains.post.service.PostService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

	private final PostLikeRepository postLikeRepository;
	private final PostService postService;
	private final EntityManager em;

	@Transactional(rollbackFor = Exception.class)
	public void likePost(LikePostParameter parameter) {
		Long postId = parameter.postId(), memberId = parameter.memberId();
		LikeType likeType = parameter.likeType();
		Post post = postService.findById(postId);
		Member member = em.getReference(Member.class, memberId);

		if (likeType == LikeType.LIKE) {
			// 라이크가 이미 존재하면 무시
			if (postLikeRepository.existsByPostIdAndMemberId(postId, memberId))
				return;

			PostLike postLike = PostLikeMapper.toEntity(member, post);
			postLikeRepository.save(postLike);
			return ;
		}

		postLikeRepository.deleteByMember_IdAndPost_Id(member.getId(), postId);
	}
}
