package aimo.backend.domains.member.dto;

public record CreateProfileImageUrlRequest(
	String memberName,
	String extension
) {
}
