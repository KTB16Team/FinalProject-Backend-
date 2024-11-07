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

	public Optional<RefreshToken> findBy(Long memberId) {
		return refreshTokenRepository.findBy(memberId);
	}

	public void save(RefreshToken refreshToken) {
		refreshTokenRepository.save(refreshToken);
	}

	public Optional<RefreshToken> findBy(String token) {
		return refreshTokenRepository.findBy(token);
	}

	public void deleteBy(Long memberId) {
		refreshTokenRepository.deleteBy(memberId);
	}

	public boolean existsBy(Long memberId) {
		return refreshTokenRepository.existsBy(memberId);
	}
}
