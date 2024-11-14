package aimo.backend.domains.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;

public interface ParentCommentRepository extends JpaRepository<ParentComment, Long> {

	Boolean existsByIdAndMember_Id(Long id, Long memberId);

	List<ParentComment> findByMemberId(Long memberId);

	List<ParentComment> findByPost_Id(Long postId);
}
