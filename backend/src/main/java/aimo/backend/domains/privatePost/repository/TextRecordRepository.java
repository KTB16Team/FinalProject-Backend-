package aimo.backend.domains.privatePost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.privatePost.entity.TextRecord;

public interface TextRecordRepository extends JpaRepository<TextRecord, Long> {
}
