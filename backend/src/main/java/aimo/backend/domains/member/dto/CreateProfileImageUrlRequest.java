package aimo.backend.domains.member.dto;

public record CreateProfileImageUrlRequest(
	String nickname,
	String extension
) {
}
