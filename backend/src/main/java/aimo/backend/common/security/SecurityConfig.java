package aimo.backend.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import aimo.backend.common.properties.SecurityProperties;
import aimo.backend.common.security.oAuth.OAuth2LoginFailureHandler;
import aimo.backend.domains.member.service.MemberPointService;
import aimo.backend.domains.member.service.MemberService;
import aimo.backend.common.security.filter.exceptionHandlingFilter.ExceptionHandlingFilter;
import aimo.backend.common.security.filter.jwtFilter.JwtAuthenticationFilter;
import aimo.backend.common.security.filter.jwtFilter.JwtTokenProvider;
import aimo.backend.common.security.filter.loginFilter.LoginFilter;
import aimo.backend.common.security.oAuth.CustomOAuth2UserService;
import aimo.backend.common.security.oAuth.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final MemberService memberService;
	private final MemberPointService memberPointService;
	private final UserDetailsService userDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

	private final UrlBasedCorsConfigurationSource ConfigurationSource;
	private final SecurityProperties securityProperties;
	private final PasswordEncoder passwordEncoder;
	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// 비활성화 목록
		http
			.csrf(AbstractHttpConfigurer::disable) // 세션을 사용안하므로 csrf 공격 없으므로 csrf 비활성화
			.httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 방식 비활성화
			.formLogin(AbstractHttpConfigurer::disable) //json을 이용하여 로그인을 하므로 기본 Login 비활성화
			.logout(LogoutConfigurer::disable) // 로그아웃 비활성화
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // 토큰을 사용하기 때문에 Session 사용 x

		// cors 설정
		http
			.cors(cors -> cors.configurationSource(ConfigurationSource));

		// url 관리
		http
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(securityProperties.getPermitUrls()).permitAll() // PERMIT_URLS만 바로 접근 가능
				.anyRequest().authenticated());

		//필터 체인 추가
		http
			.addFilterAfter(loginFilter(), LogoutFilter.class)
			.addFilterBefore(jwtAuthenticationFilter(), LoginFilter.class)
			.addFilterBefore(exceptionHandlingFilter(), JwtAuthenticationFilter.class);

		// X-frame option 해제 (h2 인메모리 DB 사용시 활성화)
		http
			.headers(headers -> headers
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
			);

		// oauth
		http
			.oauth2Login((oauth2) -> oauth2
				.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
					.userService(customOAuth2UserService))
				.successHandler(oAuth2LoginSuccessHandler)
				.failureHandler(oAuth2LoginFailureHandler));

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(provider);
	}

	@Bean
	public LoginFilter loginFilter() {
		LoginFilter loginFilter = new LoginFilter(jwtTokenProvider, memberService);

		loginFilter.setAuthenticationManager(authenticationManager());

		return loginFilter;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtTokenProvider, securityProperties, pathMatcher, memberPointService);
	}

	@Bean
	public ExceptionHandlingFilter exceptionHandlingFilter() {
		return new ExceptionHandlingFilter();
	}
}
