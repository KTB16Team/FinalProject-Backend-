package aimo.backend.domains.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.member.entity.RefreshToken;
import aimo.backend.domains.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	public void save(RefreshToken refreshToken) {
		refreshTokenRepository.save(refreshToken);
	}

	public void deleteBy(Long memberId) {
		refreshTokenRepository.deleteById(memberId);
	}
}
