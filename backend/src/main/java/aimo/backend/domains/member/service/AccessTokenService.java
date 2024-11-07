package aimo.backend.domains.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.member.entity.AccessToken;
import aimo.backend.domains.member.repository.AccessTokenBlackList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccessTokenService {

	private final AccessTokenBlackList accessTokenBlackList;

	public boolean isBlackListed(String accessToken) {
		log.info("accessToken: {}", accessTokenBlackList.existsByAccessToken(accessToken));
		return accessTokenBlackList.existsByAccessToken(accessToken);
	}

	public void save(AccessToken accessToken) {
		accessTokenBlackList.save(accessToken);
	}
}
