package aimo.backend.domains.auth.security.jwtFilter;

import static aimo.backend.common.exception.ErrorCode.*;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.properties.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final SecurityProperties securityProperties;
	private final AntPathMatcher pathMatcher;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();

		boolean result = Arrays.stream(securityProperties.getPermitUrls())
			.anyMatch(permitUrl -> pathMatcher.match(permitUrl, path));

		log.info("JwtAuthenticationFilter.shouldNotFilter({}) : {}", path, result);

		return result;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException, ApiException {

		final String refreshToken = jwtTokenProvider.extractRefreshToken(request).orElse(null);
		final String accessToken = jwtTokenProvider.extractAccessToken(request).orElse(null);

		//1. refresh토큰이 존재하며, refreshToken이 유효하면 access 토큰 재발급
		//2. refresh토큰이 존재하며, refreshToken이 유효하지 않으면 로그인 유도
		//3. access토큰이 존재하며, accessToken이 유효하면 인증
		//4. access토큰이 존재하며, accesToken이 유효하지 않으면 에러 리턴
		//5. 그 외 모든 경우는 에러 리턴

		if (refreshToken != null && jwtTokenProvider.isTokenValid(refreshToken)) {
			log.info("refresh토큰 인증 성공");
			jwtTokenProvider.checkRefreshTokenAndReIssueAccessAndRefreshToken(response, accessToken, refreshToken);
		}

		if (refreshToken != null && !jwtTokenProvider.isTokenValid(refreshToken)) {
			log.info("refresh토큰 인증 실패");
			throw ApiException.from(INVALID_REFRESH_TOKEN);
		}

		if (accessToken == null) throw ApiException.from(ACCESS_TOKEN_IS_NULL);

		if (jwtTokenProvider.isLogout(accessToken)) {
			log.info("access 토큰 만료(BlackList)");
			throw ApiException.from(INVALID_ACCESS_TOKEN);
		}

		if (!jwtTokenProvider.isTokenValid(accessToken)) {
			log.info("access 토큰 만료");
			throw ApiException.from(REISSUE_ACCESS_TOKEN);
		}

		checkLogoutToken(accessToken);
		log.info("access 토큰 인증 성공");
		Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
		saveAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

	// contextHolder에 인증정보 저장
	private void saveAuthentication(Authentication authentication) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}

	//토큰이 logout된 토큰인지 검사
	private void checkLogoutToken(String accessToken) {
		if (jwtTokenProvider.isLogout(accessToken)) {
			log.info("logout된 accessToken으로 인증 실패");
			throw ApiException.from(INVALID_ACCESS_TOKEN);
		}
	}
}
