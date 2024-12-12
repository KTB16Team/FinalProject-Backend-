package aimo.backend.common.messageQueue;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

	@Bean
	public MessageConverter messageConverter() {
		return  new Jackson2JsonMessageConverter();
	}
}