package aimo.backend.infrastructure.s3.dto.response;

public record CreatePresignedUrlResponse(
	String presignedUrl,
	String filename
) {
}
