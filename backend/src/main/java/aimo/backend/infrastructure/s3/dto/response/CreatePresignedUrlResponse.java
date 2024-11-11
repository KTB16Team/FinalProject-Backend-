package aimo.backend.infrastructure.s3.dto.response;

import aimo.backend.infrastructure.s3.dto.request.CreatePresignedUrlRequest;

public record CreatePresignedUrlResponse(
	String presignedUrl,
	String filename
) {

	public static CreatePresignedUrlResponse of(String presignedUrl, String filename) {
		return new CreatePresignedUrlResponse(presignedUrl, filename);
	}
}
