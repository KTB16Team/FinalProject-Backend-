package aimo.backend.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "ai.server")
public class AiServerProperties {

	private String domainUrl;
	private String judgementApi;
	private String speechToTextApi;
	private String accessKey;
}
