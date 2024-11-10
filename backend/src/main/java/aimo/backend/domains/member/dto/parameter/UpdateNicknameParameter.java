package aimo.backend.domains.member.dto.parameter;

public record UpdateNicknameParameter(
	Long memberId,
	String newNickname
) {
}
