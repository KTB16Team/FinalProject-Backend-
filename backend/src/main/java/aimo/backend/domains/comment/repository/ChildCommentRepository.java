package aimo.backend.domains.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.comment.entity.ChildComment;

public interface ChildCommentRepository extends JpaRepository<ChildComment, Long> {

	Boolean existsByIdAndMember_Id(Long id, Long memberId);

	Integer countByPost_Id(Long postId);
}
