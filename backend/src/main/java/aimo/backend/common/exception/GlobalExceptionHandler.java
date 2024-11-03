package aimo.backend.common.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import aimo.backend.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<Object> handleCustomException(ApiException e) {
		log.warn("handleCustomException", e);

		return makeErrorResponseEntity(e);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
		log.warn("handleIllegalArgument", e);

		ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
		return makeErrorResponseEntity(errorCode);
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException e,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		log.warn("handleIllegalArgument", e);

		List<String> messages = e.getBindingResult().getFieldErrors()
			.stream()
			.map(ex -> ex.getDefaultMessage())
			.collect(Collectors.toList());

		ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
		return makeErrorResponseEntity(errorCode, messages);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.warn("handleHttpMessageNotReadableException", ex);

		ErrorCode errorCode = ErrorCode.BAD_REQUEST;
		return makeErrorResponseEntity(errorCode);
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleAllException(Exception e) {
		log.warn("handleAllException", e);

		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		return makeErrorResponseEntity(errorCode);
	}

	// ErrorCode를 받아서 Response를 만드는 메서드
	private ResponseEntity<Object> makeErrorResponseEntity(ErrorCode errorCode) {
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.from(errorCode));
	}

	private ResponseEntity<Object> makeErrorResponseEntity(ApiException e) {
		return ResponseEntity
			.status(e.getHttpStatus())
			.body(ErrorResponse.from(e));
	}

	private ResponseEntity<Object> makeErrorResponseEntity(ErrorCode errorCode, List<String> message) {
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode, message));
	}
}