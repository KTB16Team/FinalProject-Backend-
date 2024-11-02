package aimo.backend.infrastructure.s3.dto;

import jakarta.validation.constraints.NotNull;

public record CreatePreSignedUrlRequest(
		@NotNull(message = "파일 이름은 필수입니다.")
		String fileName
) {
}
