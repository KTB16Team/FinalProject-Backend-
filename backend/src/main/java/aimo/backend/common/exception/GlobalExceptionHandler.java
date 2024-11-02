package aimo.backend.common.exception;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import aimo.backend.common.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
		return makeResponseEntity(e);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		return makeResponseEntity400(e);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<ErrorResponse> handleIOException(IOException e) {
		return makeResponseEntity5xx(e);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
		return makeResponseEntity5xx(e);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
		List<String> messages = e.getBindingResult().getFieldErrors()
			.stream()
			.map(ex -> ex.getDefaultMessage())
			.collect(Collectors.toList());

		ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
		return makeErrorResponseEntity(errorCode, messages);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		return makeResponseEntity5xx(e);
	}

	private ResponseEntity<ErrorResponse> makeResponseEntity(ApiException e) {
		return ResponseEntity
			.status(e.getHttpStatus())
			.body(ErrorResponse.of(e.getHttpStatus(), e.getMessage(), e.getCode()));
	}

	private ResponseEntity<ErrorResponse> makeResponseEntity400(Exception e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage(), ErrorCode.ILLEGAL_ARGUMENT.getCode()));
	}

	private ResponseEntity<Object> makeErrorResponseEntity(ErrorCode errorCode, List<String> message) {
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode, message));
	}

	private ResponseEntity<ErrorResponse> makeResponseEntity5xx(Exception e) {
		return ResponseEntity
			.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
			.body(ErrorResponse.of(
				ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(),
				e.getMessage(),
				ErrorCode.INTERNAL_SERVER_ERROR.getCode()));
	}
}
