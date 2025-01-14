package aimo.backend.domains.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import aimo.backend.domains.comment.entity.ParentComment;

public interface ParentCommentRepository extends JpaRepository<ParentComment, Long> {

	Boolean existsByIdAndMember_Id(Long id, Long memberId);

	List<ParentComment> findAllByMemberId(Long memberId);

	List<ParentComment>  findAllByPost_Id(Long postId);

	@Query("SELECT DISTINCT p FROM ParentComment p JOIN FETCH p.childComments WHERE p.post.id = :postId")
	List<ParentComment> findAllByPost_IdWithChildComments(Long postId);

	Integer countByPost_Id(Long postId);
}
