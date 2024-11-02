package aimo.backend.domains.member.dto;

public record LogOutRequest(
	String accessToken,
	String refreshToken
) {
}
