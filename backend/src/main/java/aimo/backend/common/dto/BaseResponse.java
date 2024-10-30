package aimo.backend.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public abstract class BaseResponse {

	private final String status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private final LocalDateTime timestamp = LocalDateTime.now();

	protected BaseResponse(HttpStatus status) {
		this.status = status.getReasonPhrase();
	}
}
