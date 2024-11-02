package aimo.backend.domains.privatePost.dto;

public record SaveAudioSuccessResponse(
	String url,
	Long size,
	String filename
) {
}
