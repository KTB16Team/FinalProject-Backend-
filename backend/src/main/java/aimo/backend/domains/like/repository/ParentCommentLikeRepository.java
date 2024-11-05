package aimo.backend.domains.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.like.entity.ParentCommentLike;

public interface ParentCommentLikeRepository extends JpaRepository<ParentCommentLike, Long> {

	void deleteByMemberIdAndParentCommentId(Long id, Long parentCommentId);

	Boolean existsByParentCommentIdAndMemberId(Long parentCommentId, Long memberId);
}
