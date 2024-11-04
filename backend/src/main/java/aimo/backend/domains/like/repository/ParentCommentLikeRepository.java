package aimo.backend.domains.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.like.entity.ParentCommentLike;

public interface ParentCommentLikeRepository extends JpaRepository<ParentCommentLike, Long> {

	void deleteByParentComment_Id(Long id, Long parentCommentId);

	Boolean existsByParentComment_IdAndMember_Id(Long parentCommentId, Long memberId);
}
