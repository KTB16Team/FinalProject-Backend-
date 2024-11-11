package aimo.backend.domains.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.like.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

	void deleteByMember_IdAndPost_Id(Long id, Long postId);

	boolean existsByPostIdAndMemberId(Long postId, Long memberId);
}
