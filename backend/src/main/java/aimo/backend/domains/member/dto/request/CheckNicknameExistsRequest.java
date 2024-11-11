package aimo.backend.domains.member.dto.request;

public record CheckNicknameExistsRequest(
	String nickname
) {
	public static CheckNicknameExistsRequest of(String nickname) {
		return new CheckNicknameExistsRequest(nickname);
	}
}
