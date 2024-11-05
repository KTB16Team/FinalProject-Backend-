package aimo.backend.domains.comment.dto.request;

public record LogoutRequest(
	String accessToken,
	String refreshToken
) {
}
