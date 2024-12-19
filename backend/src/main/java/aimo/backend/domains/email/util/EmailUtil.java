package aimo.backend.domains.email.util;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import aimo.backend.common.exception.ApiException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailUtil {

	private final JavaMailSender emailSender;

	// 발신할 이메일 데이터 세팅
	private SimpleMailMessage createEmailForm(
		String toEmail,
		String title,
		String text
	) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(title);
		message.setText(text);

		return message;
	}

	// 이메일 보내기
	@Async
	public void sendEmail(
		String toEmail,
		String title,
		String text
	) {
		SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);

		int maxRetries = 3;
		int retryCount = 0;
		boolean success = false;

		// 이메일 전송 및 실패시 재시도
		while (retryCount < maxRetries && !success) {
			try {
				emailSender.send(emailForm);  // 이메일 전송
				success = true;
			} catch (MailException ex) {
				retryCount++;
				try {
					Thread.sleep(5000);  // 5초 대기 후 재시도
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
			}
		}

		// 3번 시도 후 실패시 에러 발생
		if (!success) {
			throw ApiException.from(EMAIL_BAD_GATEWAY);
		}
	}
}
