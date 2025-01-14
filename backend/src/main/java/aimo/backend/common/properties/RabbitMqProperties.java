package aimo.backend.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqProperties {

	private String port;
	private String host;
	private String username;
	private String password;

	private Ssl ssl;

	@Getter
	@Setter
	public static class Ssl {
		private boolean enabled;
	}
}
