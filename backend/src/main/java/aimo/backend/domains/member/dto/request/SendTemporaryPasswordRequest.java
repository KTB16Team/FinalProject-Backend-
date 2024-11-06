package aimo.backend.domains.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SendTemporaryPasswordRequest(
	@NotNull(message = "nickname이 빈 문자열입니다.")
	String nickname,
	@Email(message = "email 형식이 아닙니다.")
	String email
) {
}
