package aimo.backend.domains.member.dto.request;

public record CreateProfileImageUrlRequest(
	String memberName,
	String extension
) {
}
