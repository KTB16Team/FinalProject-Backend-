package aimo.backend.domains.member.dto;

import jakarta.validation.constraints.NotNull;

public record UpdatePasswordRequest(
	@NotNull(message = "현재 비밀번호를 입력해주세요.")
	String password,
	@NotNull(message = "새 비밀번호를 입력해주세요.")
	String newPassword) {
}
