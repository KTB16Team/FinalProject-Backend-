package aimo.backend.common.mapper;

import aimo.backend.domains.privatePost.dto.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.PrivatePostResponse;
import aimo.backend.domains.privatePost.dto.SummaryAndJudgementResponse;
import aimo.backend.domains.privatePost.entity.PrivatePost;

public class PrivatePostMapper {

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

	public static PrivatePostPreviewResponse toPreviewResponse(PrivatePost privatePost){
		return new PrivatePostPreviewResponse(
			privatePost.getId(),
			privatePost.getTitle(),
			getPreview(privatePost.getSummaryAi(), 21),
			privatePost.getOriginType(),
			privatePost.getCreatedAt(),
			privatePost.getPublished()
		);
	}

	public static String getPreview(String summaryAi, Integer length){
		return summaryAi.length() > length ?
			summaryAi.substring(0, length) + "..." : summaryAi;
	}

	public static PrivatePostResponse toResponse(PrivatePost privatePost){
		return new PrivatePostResponse(
			privatePost.getTitle(),
			privatePost.getSummaryAi(),
			privatePost.getStancePlaintiff(),
			privatePost.getStanceDefendant(),
			privatePost.getJudgement(),
			privatePost.getFaultRate(),
			privatePost.getPublished()
		);
	}
}
