package aimo.backend.domains.member.dto.parameter;

public record FindMyInfoParameter(
	Long memberId
) {

	public static FindMyInfoParameter of(Long memberId) {
		return new FindMyInfoParameter(memberId);
	}
}
