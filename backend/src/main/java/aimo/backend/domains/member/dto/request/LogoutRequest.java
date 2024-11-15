package aimo.backend.domains.member.dto.request;

public record LogoutRequest(String accessToken) {

	public static LogoutRequest from(String accessToken) {
		return new LogoutRequest(accessToken);
	}
}
