package aimo.backend.domains.privatePost.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.privatePost.entity.PrivatePost;

public interface PrivatePostRepository extends JpaRepository<PrivatePost, Long> {
	Page<PrivatePost> findAllByMemberId(Long memberId, Pageable pageable);

	Optional<PrivatePost> findByMember_IdAndId(Long memberId, Long id);
}
