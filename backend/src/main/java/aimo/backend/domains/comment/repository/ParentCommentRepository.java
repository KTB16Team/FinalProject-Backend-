package aimo.backend.domains.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;

public interface ParentCommentRepository extends JpaRepository<ParentComment, Long> {

	Boolean existsByIdAndMember
		(Long id, Member member);
}
