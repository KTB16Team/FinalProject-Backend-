package aimo.backend.domains.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import aimo.backend.domains.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAllByMember_Id(Long memberId, Pageable pageable);

	Page<Post> findAllByOrderByIdDesc(Pageable pageable);

	@Query("SELECT p From Post p LEFT JOIN p.postViews pv GROUP BY p ORDER BY COUNT(pv) DESC")
	Page<Post> findByViewsCount(Pageable pageable);
}
