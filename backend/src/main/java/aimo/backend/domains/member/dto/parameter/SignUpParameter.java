package aimo.backend.domains.member.dto.parameter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import aimo.backend.domains.member.model.Gender;

public record SignUpParameter(
	String nickname,
	String email,
	String encodedPassword,
	Gender gender,
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate birth
) {
}
