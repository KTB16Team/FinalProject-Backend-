package aimo.backend.domains.privatePost.dto.response;

import aimo.backend.domains.privatePost.dto.parameter.JudgementToAiParameter;
import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record JudgementResponse(
	String title,
	String summary,
	String stancePlaintiff,
	String stanceDefendant,
	String judgement,
	Integer faultRatePlaintiff,
	Integer faultRateDefendant,
	@Enumerated(EnumType.STRING)
	OriginType originType
) {

	public static JudgementResponse of(
		String title,
		String summary,
		String stancePlaintiff,
		String stanceDefendant,
		String judgement,
		Integer faultRatePlaintiff,
		Integer faultRateDefendant,
		OriginType originType
	) {
		return new JudgementResponse(
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

	public static JudgementResponse from (JudgementFromAiResponse judgementFromAi,
		Integer faultRatePlaintiff, Integer faultRateDefendant, JudgementToAiParameter parameter) {
		return new JudgementResponse(
			judgementFromAi.title(),
			judgementFromAi.summaryAi(),
			judgementFromAi.stancePlaintiff(),
			judgementFromAi.stanceDefendant(),
			judgementFromAi.judgement(),
			100 - faultRatePlaintiff,
			100 - faultRateDefendant,
			parameter.originType());
	}
}
