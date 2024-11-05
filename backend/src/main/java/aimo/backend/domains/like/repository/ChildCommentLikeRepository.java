package aimo.backend.domains.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.like.entity.ChildCommentLike;

public interface ChildCommentLikeRepository extends JpaRepository<ChildCommentLike, Long> {

	void deleteByMemberIdAndChildCommentId(Long id, Long childCommentId);
	boolean existsByChildCommentIdAndMemberId(Long childCommentId, Long id);
}
