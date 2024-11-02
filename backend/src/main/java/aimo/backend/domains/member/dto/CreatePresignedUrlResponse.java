package aimo.backend.domains.member.dto;

public record CreatePresignedUrlResponse(
	String presignedUrl,
	String filename
) {
}
