package aimo.backend.domains.privatePost.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

public record ChatRecordRequest(
	@NotNull(message = "대화 목록 파일이 전달되지 않았습니다.")
	MultipartFile file
) {
}
