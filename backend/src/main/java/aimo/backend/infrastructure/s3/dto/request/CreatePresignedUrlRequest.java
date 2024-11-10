package aimo.backend.infrastructure.s3.dto.request;

public record CreatePresignedUrlRequest(
	String filename
) {
}
