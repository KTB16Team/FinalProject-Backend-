package aimo.backend.domains.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateNicknameRequest(
	@NotBlank String newNickname
) {

	public static UpdateNicknameRequest from(String newNickname) {
		return new UpdateNicknameRequest(newNickname);
	}
}
