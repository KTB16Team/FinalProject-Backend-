package aimo.backend.domains.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aimo.backend.domains.post.entity.PostView;

public interface PostViewRepository extends JpaRepository<PostView, Long> {
}
