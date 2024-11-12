package aimo.backend.domains.post.dto.response;

import aimo.backend.domains.post.entity.Post;
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

	public static FindJudgementResponse from(Post post) {
		return new FindJudgementResponse(
			post.getTitle(),
			post.getSummaryAi(),
			post.getStancePlaintiff(),
			post.getStanceDefendant(),
			post.getJudgement(),
			post.getFaultRatePlaintiff(),
			post.getFaultRateDefendant(),
			post.getOriginType()
		);
	}
}