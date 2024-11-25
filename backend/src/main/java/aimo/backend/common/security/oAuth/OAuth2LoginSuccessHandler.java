package aimo.backend.common.security.oAuth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import aimo.backend.common.security.dto.CustomUserDetails;
import aimo.backend.common.security.filter.jwtFilter.JwtTokenProvider;
import aimo.backend.domains.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		Member member = userDetails.getMember();

		String accessToken = jwtTokenProvider.createAccessToken(member.getId());
		String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

		jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
		jwtTokenProvider.saveOrUpdateRefreshToken(member.getId(), refreshToken);
	}
}
