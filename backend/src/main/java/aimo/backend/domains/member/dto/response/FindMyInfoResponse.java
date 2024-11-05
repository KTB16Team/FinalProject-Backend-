package aimo.backend.domains.member.dto.response;

public record FindMyInfoResponse(
	String nickname,
	String email,
	String profileImageUrl) {
}
