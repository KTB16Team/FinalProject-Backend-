package aimo.backend.domains.post.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import aimo.backend.domains.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAllByMember_Id(Long memberId, Pageable pageable);

	Page<Post> findAllByOrderByIdDesc(Pageable pageable);

	Page<Post> findAllByOrderByPostViewsCountDesc(Pageable pageable);

	Optional<Post> findById(Long postId);

	Boolean existsByIdAndMember_Id(Long postId, Long memberId);
}
