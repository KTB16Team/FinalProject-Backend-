package aimo.backend.domains.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.like.entity.ChildCommentLike;

public interface ChildCommentLikeRepository extends JpaRepository<ChildCommentLike, Long> {

	void deleteByMember_IdAndChildComment_Id(Long id, Long childCommentId);

	boolean existsByChildComment_IdAndMember_Id(Long childCommentId, Long id);
}
