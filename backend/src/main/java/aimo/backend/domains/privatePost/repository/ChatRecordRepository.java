package aimo.backend.domains.privatePost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aimo.backend.domains.privatePost.entity.ChatRecord;

public interface ChatRecordRepository extends JpaRepository<ChatRecord, Long> {
}
