package aimo.backend.domains.member.repository;

import org.springframework.data.repository.CrudRepository;

import aimo.backend.domains.member.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
