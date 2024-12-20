package aimo.backend.common.messageQueue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageQueueService {

	private final RabbitTemplate rabbitTemplate;
	private final String JUDGEMENT_EXCHANGE_NAME = "aiProcessingExchange";
	private final String JUDGEMENT_QUEUE_ROUTING_KEY = "ai.processing.key";
	private final String IMAGE_TO_TEXT_EXCHANGE_NAME = "aiOCRExchange";
	private final String IMAGE_TO_TEXT_QUEUE_ROUTING_KEY = "ai.ocr.key";
	private final String SPEECH_TO_TEXT_EXCHANGE_NAME = "aiSTTExchange";
	private final String SPEECH_TO_TEXT_QUEUE_ROUTING_KEY = "ai.stt.key";

	public void judgement(Object data) {
		rabbitTemplate.convertAndSend(JUDGEMENT_EXCHANGE_NAME, JUDGEMENT_QUEUE_ROUTING_KEY,data);
	}

	public void imageToText(Object data) {
		rabbitTemplate.convertAndSend(IMAGE_TO_TEXT_EXCHANGE_NAME, IMAGE_TO_TEXT_QUEUE_ROUTING_KEY, data);
	}

	public void speechToText(Object data) {
		rabbitTemplate.convertAndSend(SPEECH_TO_TEXT_EXCHANGE_NAME, SPEECH_TO_TEXT_QUEUE_ROUTING_KEY, data);
	}
}
