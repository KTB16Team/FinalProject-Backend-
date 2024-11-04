package aimo.backend.domains.member.dto;

public record SendTemporaryPasswordResponse(
	String to,
	String title,
	String content,
	String from
) {}
