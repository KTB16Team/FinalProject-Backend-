package aimo.backend.domains.member.dto.parameter;

public record DeleteMemberContentsParameter(Long memberId) {

	public static DeleteMemberContentsParameter from(Long memberId) {
		return new DeleteMemberContentsParameter(memberId);
	}
}
