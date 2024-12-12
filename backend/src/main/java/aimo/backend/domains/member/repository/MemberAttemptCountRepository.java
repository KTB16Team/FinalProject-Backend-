package aimo.backend.domains.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.member.entity.MemberAttemptCount;

public interface MemberAttemptCountRepository extends JpaRepository<MemberAttemptCount, Long> {
}
