package aimo.backend.common.security.oAuth;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.properties.FrontProperties;
import aimo.backend.common.util.responseWriter.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final FrontProperties frontProperties;

	@Override
	public void onAuthenticationFailure(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException exception
	) throws IOException {
		log.error("OAuth2 authentication failed", exception);

		// React의 Redirect URI로 리다이렉트
		String redirectUrl = String.format(
			"%s/500",
			frontProperties.getDomain()
		);

		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}
}
