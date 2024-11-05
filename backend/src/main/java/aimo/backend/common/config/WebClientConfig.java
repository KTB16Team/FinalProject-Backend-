package aimo.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import aimo.backend.common.properties.AiServerProperties;

@Configuration
public class WebClientConfig {
	private final AiServerProperties aiServerProperties;

	public WebClientConfig(AiServerProperties aiServerProperties) {
		this.aiServerProperties = aiServerProperties;
	}

	@Bean
	public WebClient webClient() {
		return WebClient
			.builder()
			.baseUrl(aiServerProperties.getDomainUrl())
			.build();
	}
}
