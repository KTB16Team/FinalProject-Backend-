package aimo.backend.domains.member.dto;

import java.time.LocalDate;

import aimo.backend.domains.member.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
	@NotBlank(message = "userName이 빈 문자열입니다.")
	String username,
	@Email(message = "email 형식이 아닙니다.")
	String email,
	@NotBlank(message = "password가 빈 문자열입니다.")
	String password,
	Gender gender,
	@NotBlank(message = "phoneNumber이 빈 문자열입니다.")
	String phone_number,
	LocalDate birth
) {
}
