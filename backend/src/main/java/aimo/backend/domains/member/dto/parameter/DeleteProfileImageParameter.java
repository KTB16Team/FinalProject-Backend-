package aimo.backend.domains.member.dto.parameter;

public record DeleteProfileImageParameter(Long memberId) {

	public static DeleteProfileImageParameter from(Long memberId) {
		return new DeleteProfileImageParameter(memberId);
	}
}
