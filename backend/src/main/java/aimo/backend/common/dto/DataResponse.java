package aimo.backend.common.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse<T> extends BaseResponse {

	private final T data;

	private DataResponse(HttpStatus status, T data) {
		super(status);
		this.data = data;
	}

	public static <T> DataResponse<T> from(T data) {
		return new DataResponse<>(HttpStatus.OK, data);
	}

	public static <T> DataResponse<Void> ok() {
		return new DataResponse<>(HttpStatus.OK, null);
	}

	public static <T> DataResponse<Void> created() {
		return new DataResponse<>(HttpStatus.CREATED, null);
	}

	public static <T> DataResponse<Void> noContent() {
		return new DataResponse<>(HttpStatus.NO_CONTENT, null);
	}
}
