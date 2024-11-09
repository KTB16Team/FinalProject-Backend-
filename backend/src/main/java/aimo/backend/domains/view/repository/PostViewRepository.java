package aimo.backend.domains.view.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.view.entity.PostView;

public interface PostViewRepository extends JpaRepository<PostView, Long> {

	Optional<PostView> findByMemberIdAndPostId(Long memberId, Long postId);
}
