package aimo.backend.domains.member.dto;

import jakarta.validation.constraints.Email;

public record SendTemproraryPasswordRequest(
	@Email(message = "이메일 형식이 아닙니다.")
	String email) {
}
