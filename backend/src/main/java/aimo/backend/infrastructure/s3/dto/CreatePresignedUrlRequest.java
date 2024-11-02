package aimo.backend.infrastructure.s3.dto;

import aimo.backend.util.validator.ValidFileExtension;

public record CreatePresignedUrlRequest(
	String filename
) {
}
