package aimo.backend.domains.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.like.entity.ParentCommentLike;

public interface ParentCommentLikeRepository extends JpaRepository<ParentCommentLike, Long> {

	void deleteByMember_IdAndParentComment_Id(Long likeId, Long parentCommentId);
	Boolean existsByParentComment_IdAndMember_Id(Long parentCommentId, Long memberId);
}
