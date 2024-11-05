package aimo.backend.domains.privatePost.dto.response;

public record SaveAudioSuccessResponse(
	String url,
	Long size,
	String filename
) {
}
