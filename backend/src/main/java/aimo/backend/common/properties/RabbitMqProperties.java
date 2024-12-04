package aimo.backend.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqProperties {

	private String port;
	private String host;
	private String username;
	private String password;
}
