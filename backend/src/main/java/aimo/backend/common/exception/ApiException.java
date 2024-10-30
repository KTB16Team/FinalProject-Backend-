package aimo.backend.common.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

	private final HttpStatus httpStatus;
	private final String code;

	public ApiException(HttpStatus httpStatus, String message, String code) {
		super(message);
		this.httpStatus = httpStatus;
		this.code = code;
	}

	public static ApiException from(ErrorCode errorCode) {
		return new ApiException(errorCode.getHttpStatus(), errorCode.getMessage(), errorCode.getCode());
	}

	public static ApiException of(
		HttpStatus httpStatus,
		String message,
		String code
	) {
		return new ApiException(httpStatus, message, code);
	}
}
