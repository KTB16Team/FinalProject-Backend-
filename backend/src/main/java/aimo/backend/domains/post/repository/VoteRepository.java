package aimo.backend.domains.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.post.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {

	Optional<Vote> findByPost_IdAndMember_Id(Long postId, Long memberId);
}
