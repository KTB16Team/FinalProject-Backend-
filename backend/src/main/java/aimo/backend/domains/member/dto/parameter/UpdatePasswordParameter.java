package aimo.backend.domains.member.dto.parameter;

public record UpdatePasswordParameter(
	Long memberId,
	String password,
	String newPassword
) {
}
