package aimo.backend.domains.member.dto.parameter;

public record UpdatePasswordParameter(
	Long memberId,
	String password,
	String newPassword
) {

	public static UpdatePasswordParameter of(Long memberId, String password, String newPassword) {
		return new UpdatePasswordParameter(memberId, password, newPassword);
	}
}
