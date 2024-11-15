package aimo.backend.domains.member.dto.request;

public record UpdateNicknameRequest(String newNickname) {

	public static UpdateNicknameRequest from(String newNickname) {
		return new UpdateNicknameRequest(newNickname);
	}
}
