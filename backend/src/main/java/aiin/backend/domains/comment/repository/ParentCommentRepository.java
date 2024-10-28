package aiin.backend.domains.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aiin.backend.domains.comment.entity.ParentComment;
import aiin.backend.domains.member.entity.Member;

public interface ParentCommentRepository extends JpaRepository<ParentComment, Long> {

	Boolean existsByIdAndMember
		(Long id, Member member);
}
