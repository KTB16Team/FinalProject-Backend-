package aimo.backend.domains.member.dto.parameter;

public record UpdateNicknameParameter(
	Long memberId,
	String newNickname
) {

	public static UpdateNicknameParameter of(Long memberId, String newNickname) {
		return new UpdateNicknameParameter(memberId, newNickname);
	}
}
