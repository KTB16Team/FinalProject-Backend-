package aiin.backend.domains.dispute.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aiin.backend.domains.dispute.entity.DialogueRecord;

public interface DialogueRepository extends JpaRepository<DialogueRecord, Long> {
}
