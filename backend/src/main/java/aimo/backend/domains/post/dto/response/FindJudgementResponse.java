package aimo.backend.domains.post.dto.response;

import aimo.backend.domains.privatePost.model.OriginType;

public record FindJudgementResponse(
	String title,
	String summary,
	String stancePlaintiff,
	String stanceDefendant,
	String judgement,
	Integer faultRatePlaintiff,
	Integer faultRateDefendant,
	OriginType originType
) {
	public static FindJudgementResponse of(
		String title,
		String summary,
		String stancePlaintiff,
		String stanceDefendant,
		String judgement,
		Integer faultRatePlaintiff,
		Integer faultRateDefendant,
		OriginType originType
	) {
		return new FindJudgementResponse(
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
}