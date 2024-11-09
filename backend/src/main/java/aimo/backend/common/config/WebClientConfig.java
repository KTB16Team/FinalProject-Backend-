package aimo.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import aimo.backend.common.properties.AiServerProperties;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
	private final AiServerProperties aiServerProperties;

	@Bean
	public WebClient webClient() {
		return WebClient
			.builder()
			.baseUrl(aiServerProperties.getDomainUrl())
			.build();
	}
}
