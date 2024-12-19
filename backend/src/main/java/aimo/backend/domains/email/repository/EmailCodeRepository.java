package aimo.backend.domains.email.repository;

import org.springframework.data.repository.CrudRepository;

import aimo.backend.domains.email.domain.EmailCode;

public interface EmailCodeRepository extends CrudRepository<EmailCode, String> {
}
