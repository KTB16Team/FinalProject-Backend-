package aimo.backend.domains.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.like.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

	void deleteByMemberIdAndPostId(Long id, Long postId);

	boolean existsByPostIdAndMemberId(Long postId, Long memberId);
}
