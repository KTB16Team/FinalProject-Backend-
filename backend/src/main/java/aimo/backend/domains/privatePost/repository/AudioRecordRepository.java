package aimo.backend.domains.privatePost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.privatePost.entity.AudioRecord;

public interface AudioRecordRepository extends JpaRepository<AudioRecord, Long> {
}
