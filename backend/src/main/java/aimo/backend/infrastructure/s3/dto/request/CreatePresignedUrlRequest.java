package aimo.backend.infrastructure.s3.dto.request;

public record CreatePresignedUrlRequest(
	String filename
) {

	public static CreatePresignedUrlRequest of(String filename) {
		return new CreatePresignedUrlRequest(filename);
	}
}
