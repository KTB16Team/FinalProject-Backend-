package aimo.backend.infrastructure.s3.dto.request;

import jakarta.validation.constraints.NotNull;

public record SaveFileMetaDataRequest(
	@NotNull(message = "파일 이름은 필수입니다.")
	String filename,
	@NotNull(message = "파일 확장자는 필수입니다.")
	String extension,
	@NotNull(message = "파일 크기는 필수입니다.")
	Long size
) {
}
