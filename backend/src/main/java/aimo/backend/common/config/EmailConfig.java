package aimo.backend.common.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import aimo.backend.common.properties.EmailProperties;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

	private final EmailProperties emailProperties;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(emailProperties.getHost());
		mailSender.setPort(emailProperties.getPort());
		mailSender.setUsername(emailProperties.getUsername());
		mailSender.setPassword(emailProperties.getPassword());
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setJavaMailProperties(getMailProperties());

		return mailSender;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.starttls.enable", true);
		properties.put("mail.smtp.starttls.required", true);
		properties.put("mail.smtp.connectiontimeout", emailProperties.getConnectionTimeout());
		properties.put("mail.smtp.timeout", emailProperties.getTimeout());
		properties.put("mail.smtp.writetimeout", emailProperties.getWriteTimeout());

		return properties;
	}
}
