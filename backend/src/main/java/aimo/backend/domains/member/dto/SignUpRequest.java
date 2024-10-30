package aimo.backend.domains.member.dto;

import java.time.LocalDate;

import aimo.backend.domains.member.model.Gender;

public record SignUpRequest(
	String username,
	String email,
	String password,
	Gender gender,
	String phoneNumber,
	LocalDate birth)
{
}
