package aimo.backend.infrastructure.s3.dto.response;

public record CreatePreSignedUrlResponse(
	String preSignedUrl,
	String filename,
	String key
) {

	public static CreatePreSignedUrlResponse of(String preSignedUrl, String filename, String key) {
		return new CreatePreSignedUrlResponse(preSignedUrl, filename, key);
	}
}
