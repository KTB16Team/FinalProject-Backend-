package aimo.backend.domains.member.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import aimo.backend.domains.member.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

	Optional<RefreshToken> findBy(Long memberId);
	Optional<RefreshToken> findBy(String refreshToken);
	boolean existsBy(Long memberId);
	void deleteBy(Long memberId);
}
