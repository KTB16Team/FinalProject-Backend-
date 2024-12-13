package aimo.backend.domains.upload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.upload.entity.FileRecord;

public interface FileRepository extends JpaRepository<FileRecord, Long> {
}
