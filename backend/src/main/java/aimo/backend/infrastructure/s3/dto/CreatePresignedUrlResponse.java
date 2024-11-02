package aimo.backend.infrastructure.s3.dto;

public record CreatePresignedUrlResponse(
	String presignedUrl,
	String filename
) {
}
