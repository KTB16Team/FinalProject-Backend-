package aimo.backend.domains.member.dto;

import java.time.LocalDate;

import aimo.backend.domains.member.model.Gender;

public record SignUpRequest(
	String memberName,
	String email,
	String password,
	Gender gender,
	LocalDate birth) {
}
