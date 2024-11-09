package aimo.backend.domains.member.dto.request;

import jakarta.validation.constraints.Email;

public record SendTemporaryPasswordRequest(
	@Email(message = "이메일 형식이 아닙니다.")
	String email) {
}
