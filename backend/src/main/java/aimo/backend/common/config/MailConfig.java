package aimo.backend.common.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import aimo.backend.common.properties.MailProperties;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

	private final MailProperties mailProperties;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailProperties.getHost());
		mailSender.setPort(mailProperties.getPort());
		mailSender.setUsername(mailProperties.getUsername());
		mailSender.setPassword(mailProperties.getPassword());
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setJavaMailProperties(getMailProperties());

		return mailSender;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", mailProperties.getAuth());
		properties.put("mail.smtp.starttls.enable", mailProperties.getStarttlsEnable());
		properties.put("mail.smtp.starttls.required", mailProperties.getStarttlsRequired());
		properties.put("mail.smtp.connectiontimeout", mailProperties.getConnectionTimeout());
		properties.put("mail.smtp.timeout", mailProperties.getTimeout());
		properties.put("mail.smtp.writetimeout", mailProperties.getWriteTimeout());

		return properties;
	}
}
