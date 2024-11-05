package aimo.backend.domains.privatePost.dto.response;

public record AudioRecordPresignedResponse(
	String presignedUrl,
	String filename
) {
}
