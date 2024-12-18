package aimo.backend.domains.member.dto.parameter;

import java.time.LocalDate;

import aimo.backend.domains.member.dto.request.SignUpRequest;
import aimo.backend.domains.member.model.Gender;

public record SignUpParameter(
	Integer code,
	String nickname,
	String email,
	String password,
	Gender gender,
	LocalDate birth
) {

	public static SignUpParameter from(SignUpRequest request) {
		return new SignUpParameter(
			request.code(),
			request.nickname(),
			request.email(),
			request.password(),
			request.gender(),
			request.birth()
		);
	}
}
