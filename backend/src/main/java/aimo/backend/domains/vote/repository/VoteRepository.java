package aimo.backend.domains.vote.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.vote.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {

	Optional<Vote> findByPostIdAndMemberId(Long postId, Long memberId);
}
