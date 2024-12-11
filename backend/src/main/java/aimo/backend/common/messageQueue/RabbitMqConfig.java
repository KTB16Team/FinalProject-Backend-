package aimo.backend.common.messageQueue;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import aimo.backend.common.properties.RabbitMqProperties;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

	private final RabbitMqProperties properties;

	@Bean
	public ConnectionFactory connectionFactory() throws NoSuchAlgorithmException, KeyManagementException {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setPort(Integer.parseInt(properties.getPort()));
		connectionFactory.setHost(properties.getHost());
		connectionFactory.setUsername(properties.getUsername());
		connectionFactory.setPassword(properties.getPassword());

		// SSL 설정 추가
		if (properties.getSsl().isEnabled()) { // RabbitMqProperties에 sslEnabled 플래그 추가 필요
			connectionFactory.getRabbitConnectionFactory().useSslProtocol();
		}

		return connectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() throws NoSuchAlgorithmException, KeyManagementException {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory());
		rabbitTemplate.setMessageConverter(messageConverter());
		return rabbitTemplate;
	}

	@Bean
	public MessageConverter messageConverter() {
		return  new Jackson2JsonMessageConverter();
	}
}