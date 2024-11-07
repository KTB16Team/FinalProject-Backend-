package aimo.backend.domains.member.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import aimo.backend.domains.member.entity.AccessToken;

@Repository
public interface AccessTokenBlackList extends CrudRepository<AccessToken, Long> {

	boolean existsByMemberId(Long memberId);
	boolean existsByAccessToken(String accessToken);
}
