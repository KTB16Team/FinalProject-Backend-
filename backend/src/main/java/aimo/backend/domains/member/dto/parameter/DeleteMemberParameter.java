package aimo.backend.domains.member.dto.parameter;

public record DeleteMemberParameter(
	Long memberId,
	String password
) {

	public static DeleteMemberParameter of(Long memberId, String password) {
		return new DeleteMemberParameter(memberId, password);
	}
}
