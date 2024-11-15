package aimo.backend.domains.member.dto.parameter;

import aimo.backend.domains.member.dto.request.UpdatePasswordRequest;

public record UpdatePasswordParameter(
	Long memberId,
	String password,
	String newPassword
) {

	public static UpdatePasswordParameter of(Long memberId, String password, String newPassword) {
		return new UpdatePasswordParameter(memberId, password, newPassword);
	}

	public static UpdatePasswordParameter of(Long memberId, UpdatePasswordRequest updatePasswordRequest) {
		return new UpdatePasswordParameter(
			memberId,
			updatePasswordRequest.password(),
			updatePasswordRequest.newPassword()
		);
	}
}
