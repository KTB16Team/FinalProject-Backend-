package aimo.backend.domains.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SendTemporaryPasswordRequest(
	@NotNull(message = "닉네임을 입력해주세요.")
	String nickname,
	@Email(message = "이메일 형식이 아닙니다.")
	String email) {
}
