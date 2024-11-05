package aimo.backend.domains.member.dto.response;

public record SendTemporaryPasswordResponse(
	String to,
	String title,
	String content,
	String from
) {}
