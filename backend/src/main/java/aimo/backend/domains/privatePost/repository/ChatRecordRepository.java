package aimo.backend.domains.privatePost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.privatePost.entity.ChatRecord;

public interface ChatRecordRepository extends JpaRepository<ChatRecord, Long> {
}
