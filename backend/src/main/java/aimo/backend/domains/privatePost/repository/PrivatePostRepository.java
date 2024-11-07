package aimo.backend.domains.privatePost.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.privatePost.entity.PrivatePost;

public interface PrivatePostRepository extends JpaRepository<PrivatePost, Long> {
	
	Page<PrivatePost> findByMemberId(Long memberId, Pageable pageable);
}
