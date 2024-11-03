package aimo.backend.domains.privatePost.dto;

public record AudioRecordPresignedResponse(
	String presignedUrl,
	String filename
) {
}
