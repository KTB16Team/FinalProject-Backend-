package aimo.backend.domains.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.member.entity.Member;

public interface ChildCommentRepository extends JpaRepository<ChildComment, Long> {

	Boolean existsByIdAndMember(Long id, Member member);
}
