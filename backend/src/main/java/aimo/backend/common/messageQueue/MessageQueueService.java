package aimo.backend.common.messageQueue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageQueueService {

	private final RabbitTemplate rabbitTemplate;
	private final String DIRECT_EXCHANGE_NAME = "direct_exchange"; // 추후 변경
	private final String DIRECT_QUEUE_ROUTING_KEY = "direct_routing_key"; // 추후 변경

	public void send(Object data) {
		rabbitTemplate.convertAndSend(DIRECT_EXCHANGE_NAME,DIRECT_QUEUE_ROUTING_KEY,data);
	}
}
