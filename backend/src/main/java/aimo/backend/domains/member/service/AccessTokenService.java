package aimo.backend.domains.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.member.entity.AccessToken;
import aimo.backend.domains.member.repository.AccessTokenBlackList;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccessTokenService {

	private final AccessTokenBlackList accessTokenBlackList;

	@Transactional
	public void save(AccessToken accessToken) {
		accessTokenBlackList.save(accessToken);
	}

	public boolean isBlackListed(String accessToken) {
		return accessTokenBlackList.existsByAccessToken(accessToken);
	}
}
