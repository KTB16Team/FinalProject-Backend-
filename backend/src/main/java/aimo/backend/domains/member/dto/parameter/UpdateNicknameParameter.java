package aimo.backend.domains.member.dto.parameter;

import aimo.backend.domains.member.dto.request.UpdateNicknameRequest;

public record UpdateNicknameParameter(
	Long memberId,
	String newNickname
) {

	public static UpdateNicknameParameter of(Long memberId, String newNickname) {
		return new UpdateNicknameParameter(memberId, newNickname);
	}

	public static UpdateNicknameParameter of(Long memberId, UpdateNicknameRequest request) {
		return new UpdateNicknameParameter(
			memberId,
			request.newNickname()
		);
	}
}
