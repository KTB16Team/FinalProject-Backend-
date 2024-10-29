package aiin.backend.domains.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aiin.backend.domains.comment.entity.ChildComment;
import aiin.backend.domains.member.entity.Member;

public interface ChildCommentRepository extends JpaRepository<ChildComment, Long> {

	Boolean existsByIdAndMember(Long id, Member member);
}
