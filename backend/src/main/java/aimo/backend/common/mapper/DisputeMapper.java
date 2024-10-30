package aimo.backend.common.mapper;

import aimo.backend.domains.privatePost.dto.SummaryAndJudgementResponse;
import aimo.backend.domains.privatePost.entity.PrivatePost;

public class DisputeMapper {

	public static PrivatePost toEntity(SummaryAndJudgementResponse summaryAndJudgementResponse) {
		return PrivatePost
			.builder()
			.title(summaryAndJudgementResponse.title())
			.stancePlaintiff(summaryAndJudgementResponse.stancePlaintiff())
			.stanceDefendant(summaryAndJudgementResponse.stanceDefendant())
			.summaryAi(summaryAndJudgementResponse.summaryAi())
			.judgement(summaryAndJudgementResponse.judgement())
			.faultRate(summaryAndJudgementResponse.faultRate())
			.originType(summaryAndJudgementResponse.originType())
			.build();
	}
}
