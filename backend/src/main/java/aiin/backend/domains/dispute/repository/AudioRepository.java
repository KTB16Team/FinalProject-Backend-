package aiin.backend.domains.dispute.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aiin.backend.domains.dispute.entity.AudioRecord;

public interface AudioRepository extends JpaRepository<AudioRecord, Long> {
}
