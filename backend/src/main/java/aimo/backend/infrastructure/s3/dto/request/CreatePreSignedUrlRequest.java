package aimo.backend.infrastructure.s3.dto.request;

public record CreatePreSignedUrlRequest(
	String filename
) {

	public static CreatePreSignedUrlRequest of(String filename) {
		return new CreatePreSignedUrlRequest(filename);
	}
}
