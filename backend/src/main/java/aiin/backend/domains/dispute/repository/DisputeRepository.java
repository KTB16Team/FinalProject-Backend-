package aiin.backend.domains.dispute.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aiin.backend.domains.dispute.entity.Dispute;

public interface DisputeRepository extends JpaRepository<Dispute, Long> {
}
