package aimo.backend.infrastructure.s3.dto;

import jakarta.validation.constraints.NotNull;

public record SaveProfileImageMetaDataParameter(
	Long memberId,
	String filename,
	String extension,
	Long size
) {
}
