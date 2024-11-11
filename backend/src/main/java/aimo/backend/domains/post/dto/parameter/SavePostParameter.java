package aimo.backend.domains.post.dto.parameter;

import aimo.backend.domains.post.dto.requset.SavePostRequest;
import aimo.backend.domains.post.model.Category;
import aimo.backend.domains.privatePost.model.OriginType;

public record SavePostParameter(
	Long memberId,
	Long privatePostId,
	String title,
	String stancePlaintiff,
	String stanceDefendant,
	String summaryAi,
	String judgement,
	Integer faultRateDefendant,
	Integer faultRatePlaintiff,
	OriginType originType,
	Category category
) {

	public static SavePostParameter of(
		Long memberId,
		Long privatePostId,
		String title,
		String stancePlaintiff,
		String stanceDefendant,
		String summaryAi,
		String judgement,
		Integer faultRateDefendant,
		Integer faultRatePlaintiff,
		OriginType originType,
		Category category
	) {
		return new SavePostParameter(
			memberId,
			privatePostId,
			title,
			stancePlaintiff,
			stanceDefendant,
			summaryAi,
			judgement,
			faultRateDefendant,
			faultRatePlaintiff,
			originType,
			category);
	}

	public static SavePostParameter from(Long memberId, SavePostRequest request) {
		return new SavePostParameter(
			memberId,
			request.privatePostId(),
			request.title(),
			request.stancePlaintiff(),
			request.stanceDefendant(),
			request.summaryAi(),
			request.judgement(),
			request.faultRateDefendant(),
			request.faultRatePlaintiff(),
			request.originType(),
			request.category());
	}
}