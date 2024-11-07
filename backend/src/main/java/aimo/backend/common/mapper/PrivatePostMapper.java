package aimo.backend.common.mapper;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.privatePost.dto.response.JudgementResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostResponse;
import aimo.backend.domains.privatePost.entity.PrivatePost;

public class PrivatePostMapper {

	public static PrivatePost toEntity(JudgementResponse judgementResponse, Member member) {
		return PrivatePost.builder()
			.title(judgementResponse.title())
			.member(member)
			.stancePlaintiff(judgementResponse.stancePlaintiff())
			.stanceDefendant(judgementResponse.stanceDefendant())
			.summaryAi(judgementResponse.summary())
			.judgement(judgementResponse.judgement())
			.faultRatePlaintiff(judgementResponse.faultRatePlaintiff())
			.faultRateDefendant(judgementResponse.faultRateDefendant())
			.originType(judgementResponse.originType())
			.published(false)
			.build();
	}

	public static JudgementResponse toJudgement(PrivatePost privatePost) {
		return new JudgementResponse(
			privatePost.getTitle(),
			privatePost.getSummaryAi(),
			privatePost.getStancePlaintiff(),
			privatePost.getStanceDefendant(),
			privatePost.getJudgement(),
			privatePost.getFaultRatePlaintiff(),
			privatePost.getFaultRateDefendant(),
			privatePost.getOriginType());
	}

	public static PrivatePostPreviewResponse toPreviewResponse(PrivatePost privatePost) {
		return new PrivatePostPreviewResponse(privatePost.getId(), privatePost.getTitle(),
			getPreview(privatePost.getSummaryAi(), 21), privatePost.getOriginType(), privatePost.getCreatedAt(),
			privatePost.getPublished());
	}

	public static String getPreview(String summaryAi, Integer length) {
		return summaryAi.length() > length ? summaryAi.substring(0, length) + "..." : summaryAi;
	}

	public static PrivatePostResponse toResponse(PrivatePost privatePost) {
		return new PrivatePostResponse(privatePost.getId(), privatePost.getTitle(), privatePost.getSummaryAi(),
			privatePost.getStancePlaintiff(), privatePost.getStanceDefendant(), privatePost.getJudgement(),
			privatePost.getFaultRatePlaintiff(), privatePost.getFaultRateDefendant(), privatePost.getPublished());
	}
}
