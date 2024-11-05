package aimo.backend.domains.member.dto.request;

public record CreateProfileImageUrlRequest(
	String nickname,
	String extension
) {
}
