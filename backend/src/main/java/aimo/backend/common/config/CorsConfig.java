package aimo.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import aimo.backend.common.properties.CorsProperties;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {

	private final CorsProperties corsProperties;

	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSources() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(corsProperties.getAllowedOrigins());
		config.setAllowedMethods(corsProperties.getAllowedMethods());
		config.setAllowCredentials(corsProperties.getRequireCredential());
		config.setAllowedHeaders(corsProperties.getAllowedHeaders());
		config.setMaxAge(3600L); //1시간

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		// 허용 url 등록
		corsProperties.getAllowedUrls()
			.forEach(url -> source.registerCorsConfiguration(url, config));

		return source;
	}
}
