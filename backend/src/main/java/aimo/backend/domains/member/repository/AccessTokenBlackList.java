package aimo.backend.domains.member.repository;

import org.springframework.data.repository.CrudRepository;

import aimo.backend.domains.member.entity.AccessToken;

public interface AccessTokenBlackList extends CrudRepository<AccessToken, Long> {
	boolean existsByAccessToken(String accessToken);
}
