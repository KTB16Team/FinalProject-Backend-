package aimo.backend.domains.member.dto.parameter;

public record FindMyInfoParameter(
	Long memberId
) {

	public static FindMyInfoParameter from(Long memberId) {
		return new FindMyInfoParameter(memberId);
	}
}
