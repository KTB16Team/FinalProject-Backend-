package aimo.backend.domains.member.dto.request;

public record LogOutRequest(
	String accessToken,
	String refreshToken
) {
}
