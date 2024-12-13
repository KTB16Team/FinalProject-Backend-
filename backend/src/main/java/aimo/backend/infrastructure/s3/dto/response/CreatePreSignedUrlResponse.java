package aimo.backend.infrastructure.s3.dto.response;

public record CreatePreSignedUrlResponse(
	String preSignedUrl,
	String filename
) {

	public static CreatePreSignedUrlResponse of(String preSignedUrl, String filename) {
		return new CreatePreSignedUrlResponse(preSignedUrl, filename);
	}
}
