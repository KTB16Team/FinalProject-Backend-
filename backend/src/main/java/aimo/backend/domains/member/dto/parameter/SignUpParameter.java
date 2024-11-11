package aimo.backend.domains.member.dto.parameter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import aimo.backend.domains.member.dto.request.SignUpRequest;
import aimo.backend.domains.member.model.Gender;

public record SignUpParameter(
	String nickname,
	String email,
	String password,
	Gender gender,
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate birth
) {

	public static SignUpParameter from(SignUpRequest request) {
		return new SignUpParameter(
			request.nickname(),
			request.email(),
			request.password(),
			request.gender(),
			request.birth()
		);
	}
}
