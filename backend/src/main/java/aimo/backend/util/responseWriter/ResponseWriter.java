package aimo.backend.util.responseWriter;

import java.io.IOException;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import aimo.backend.common.dto.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;

public class ResponseWriter {

	private static final String CONTENT_TYPE = "application/json";
	private static final String CHARACTER_ENCODING = "utf-8";
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void writeResponse(
		HttpServletResponse response,
		BaseResponse dataResponse,
		HttpStatusCode statusCode
	) {
		response.setContentType(CONTENT_TYPE);
		response.setCharacterEncoding(CHARACTER_ENCODING);
		objectMapper.registerModule(new JavaTimeModule());
		response.setStatus(statusCode.value());

		try {
			String result = objectMapper.writeValueAsString(dataResponse);
			response.getWriter().write(result);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

