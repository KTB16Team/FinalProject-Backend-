package aimo.backend.domains.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import aimo.backend.domains.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAllByMember_Id(Long memberId, Pageable pageable);

	Page<Post> findAllByOrderByIdDesc(Pageable pageable);

	Page<Post> findAllByOrderByPostViewsCountDesc(Pageable pageable);

	Boolean existsByIdAndMember_Id(Long postId, Long memberId);

	@Query("""
		    SELECT DISTINCT p
		    FROM Post p
		    LEFT JOIN ParentComment pc ON pc.post.id = p.id
		    LEFT JOIN ChildComment cc ON cc.post.id = p.id
		    WHERE pc.member.id = :memberId OR cc.member.id = :memberId
		""")
	Page<Post> findPostsByCommentsWrittenByMember(@Param("memberId") Long memberId, Pageable pageable);
}
