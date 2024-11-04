package aimo.backend.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "mail")
@RequiredArgsConstructor
public class MailProperties {
	private final String host;
	private final int port;
	private final String username;
	private final String password;
	private final String auth;
	private final String starttlsEnable;
	private final String starttlsRequired;
	private final int connectionTimeout;
	private final int timeout;
	private final int writeTimeout;
}
