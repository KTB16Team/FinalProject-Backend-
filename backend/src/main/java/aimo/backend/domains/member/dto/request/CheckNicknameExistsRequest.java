package aimo.backend.domains.member.dto.request;

public record CheckNicknameExistsRequest(
	String nickname
) {
	public static CheckNicknameExistsRequest from(String nickname) {
		return new CheckNicknameExistsRequest(nickname);
	}
}
