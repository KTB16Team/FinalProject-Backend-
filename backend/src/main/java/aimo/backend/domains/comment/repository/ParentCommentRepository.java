package aimo.backend.domains.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.comment.entity.ParentComment;

public interface ParentCommentRepository extends JpaRepository<ParentComment, Long> {

	Boolean existsByIdAndMember_Id(Long id, Long memberId);

	List<ParentComment> findAllByMemberId(Long memberId);
}
