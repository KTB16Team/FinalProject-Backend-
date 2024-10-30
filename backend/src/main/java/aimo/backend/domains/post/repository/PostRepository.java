package aimo.backend.domains.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
