package aimo.backend.domains.member.dto.request;

public record UpdateNicknameRequest(String newNickname) {

	public static UpdateNicknameRequest of(String newNickname) {
		return new UpdateNicknameRequest(newNickname);
	}
}
