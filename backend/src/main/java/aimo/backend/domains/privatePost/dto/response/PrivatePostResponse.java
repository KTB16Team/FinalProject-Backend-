package aimo.backend.domains.privatePost.dto.response;

import aimo.backend.domains.privatePost.entity.PrivatePost;
import jakarta.validation.constraints.NotNull;

public record PrivatePostResponse(
	Long postId,
	String title,
	String summaryAi,
	String stancePlaintiff,
	String stanceDefendant,
	Integer faultRatePlaintiff,
	Integer faultRateDefendant,
	String judgement,
	Boolean published
) {

	public static PrivatePostResponse of(
		Long postId,
		String title,
		String summaryAi,
		String stancePlaintiff,
		String stanceDefendant,
		Integer faultRatePlaintiff,
		Integer faultRateDefendant,
		String judgement,
		Boolean published
	) {
		return new PrivatePostResponse(
			postId,
			title,
			summaryAi,
			stancePlaintiff,
			stanceDefendant,
			faultRatePlaintiff,
			faultRateDefendant,
			judgement,
			published
		);
	}

	public static PrivatePostResponse from(PrivatePost privatePost) {
		return new PrivatePostResponse(
			privatePost.getId(),
			privatePost.getTitle(),
			privatePost.getSummaryAi(),
			privatePost.getStancePlaintiff(),
			privatePost.getStanceDefendant(),
			privatePost.getFaultRatePlaintiff(),
			privatePost.getFaultRateDefendant(),
			privatePost.getJudgement(),
			privatePost.getPublished()
		);
	}
}
