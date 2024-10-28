package aiin.backend.domains.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aiin.backend.domains.comment.entity.ChildComment;

public interface ChildCommentRepository extends JpaRepository<ChildComment, Long> {
}
