package aimo.backend.infrastructure.smtp;

import java.util.UUID;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import aimo.backend.domains.member.dto.request.SendTemporaryPasswordRequest;
import aimo.backend.domains.member.dto.response.SendTemporaryPasswordResponse;
import aimo.backend.infrastructure.smtp.model.Notice;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender javaMailSender;
	private final SpringTemplateEngine templateEngine;

	public SendTemporaryPasswordResponse createMail(SendTemporaryPasswordRequest sendTemporaryPasswordRequest) {

		return new SendTemporaryPasswordResponse(
			sendTemporaryPasswordRequest.email(),
			Notice.TITLE.getValue(),
			Notice.INTRODUCE.getValue() + Notice.TEMPORARY_PASSWORD.getValue() + createTempPassword() + Notice.END_MESSAGE.getValue() + Notice.FROM.getValue(),
			Notice.SERVICE_ADDRESS.getValue());
	}

	private static String createTempPassword() {
		return UUID.randomUUID().toString().substring(0, 8);
	}

	public void sendMail(SendTemporaryPasswordResponse sendTemporaryPasswordResponse) throws MessagingException {
		// 메일 전송 로직
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(sendTemporaryPasswordResponse.to());
		helper.setSubject(sendTemporaryPasswordResponse.title());
		helper.setFrom(sendTemporaryPasswordResponse.from());
		helper.setReplyTo(sendTemporaryPasswordResponse.from());
		helper.setSubject(sendTemporaryPasswordResponse.title());

		Context context = new Context();
		context.setVariable("code", createTempPassword());

		String html = templateEngine.process("password", context);
		helper.setText(html, true);


		javaMailSender.send(message);
	}
}
