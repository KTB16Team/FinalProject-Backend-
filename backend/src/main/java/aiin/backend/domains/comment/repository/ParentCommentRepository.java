package aiin.backend.domains.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aiin.backend.domains.comment.entity.ParentComment;

public interface ParentCommentRepository extends JpaRepository<ParentComment, Long> {
}
