package aimo.backend.domains.member.dto.parameter;

public record DeleteMemberParameter(
	Long memberId,
	String password
) {
}
