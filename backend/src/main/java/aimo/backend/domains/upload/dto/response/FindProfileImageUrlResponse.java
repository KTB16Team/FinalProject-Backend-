package aimo.backend.domains.upload.dto.response;

public record FindProfileImageUrlResponse(
	String url
) {

	public static FindProfileImageUrlResponse from(String url) {
		return new FindProfileImageUrlResponse(url);
	}
}
