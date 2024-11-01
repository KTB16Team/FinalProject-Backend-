package aimo.backend.domains.post.repository;

import java.beans.JavaBean;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aimo.backend.domains.post.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
