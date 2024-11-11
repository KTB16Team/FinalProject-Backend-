package aimo.backend.domains.privatePost.dto.parameter;

import aimo.backend.domains.privatePost.dto.response.JudgementFromAiResponse;
import aimo.backend.domains.privatePost.dto.response.JudgementResponse;
import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record JudgementParameter(
	Long memberId,
	String title,
	String summary,
	String stancePlaintiff,
	String stanceDefendant,
	String judgement,
	Integer faultRatePlaintiff,
	Integer faultRateDefendant,
	OriginType originType
) {

	public static JudgementParameter of(
		Long memberId,
		String title,
		String summary,
		String stancePlaintiff,
		String stanceDefendant,
		String judgement,
		Integer faultRatePlaintiff,
		Integer faultRateDefendant,
		OriginType originType
	) {
		return new JudgementParameter(
			memberId,
			title,
			summary,
			stancePlaintiff,
			stanceDefendant,
			judgement,
			faultRatePlaintiff,
			faultRateDefendant,
			originType
		);
	}

	public static JudgementParameter from(
		Long memberId,
		JudgementResponse judgementResponse
	) {
		return new JudgementParameter(
			memberId,
			judgementResponse.title(),
			judgementResponse.summary(),
			judgementResponse.stancePlaintiff(),
			judgementResponse.stanceDefendant(),
			judgementResponse.judgement(),
			judgementResponse.faultRatePlaintiff(),
			judgementResponse.faultRateDefendant(),
			judgementResponse.originType()
		);
	}
}
