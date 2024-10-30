package aimo.backend.domains.privatePost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.privatePost.entity.PrivatePost;

public interface PrivatePostRepository extends JpaRepository<PrivatePost, Long> {
}
