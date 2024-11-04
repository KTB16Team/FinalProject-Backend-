package aimo.backend.domains.member.dto;

public record FindMyInfoResponse(
	String nickname,
	String email,
	String profileImageUrl) {
}
