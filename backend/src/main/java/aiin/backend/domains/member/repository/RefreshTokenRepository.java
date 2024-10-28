package aiin.backend.domains.member.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import aiin.backend.domains.member.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

	Optional<RefreshToken> findByAccessToken(String accessToken);
	boolean existsByAccessToken(String accessToken);
	void deleteByAccessToken(String accessToken);
}
