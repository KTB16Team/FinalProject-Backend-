package aimo.backend.domains.post.dto.requset;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import aimo.backend.domains.post.model.Category;
import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SavePostRequest(
	@NotNull(message = "개인게시글 아이디가 null입니다.")
	Long privatePostId,
	@NotBlank(message = "title이 null이거나 빈 문자열입니다.")
	String title,
	@NotBlank(message = "stancePlaintiff이 null이거나 빈 문자열입니다.")
	String stancePlaintiff,
	@NotBlank(message = "stanceDefendant이 null이거나 빈 문자열입니다.")
	String stanceDefendant,
	@NotBlank(message = "summaryAi이 null이거나 빈 문자열입니다.")
	String summaryAi,
	@NotBlank(message = "judgement이 null이거나 빈 문자열입니다.")
	String judgement,
	@NotNull(message = "원고 측 과실비율이 누락됐습니다.")
	Integer faultRatePlaintiff,
	@NotNull(message = "피고 측 과실비율이 누락됐습니다.")
	Integer faultRateDefendant,
	@NotNull(message = "originType이 null입니다.")
	OriginType originType,
	@JsonSetter(nulls = Nulls.SKIP)
	Category category
) {

	public SavePostRequest {
		if (category == null)
			category = Category.COMMON;
	}

	public static SavePostRequest of(
		Long privatePostId,
		String title,
		String stancePlaintiff,
		String stanceDefendant,
		String summaryAi,
		String judgement,
		Integer faultRatePlaintiff,
		Integer faultRateDefendant,
		OriginType originType,
		Category category
	) {
		if (category == null) category = Category.COMMON;

		return new SavePostRequest(
			privatePostId,
			title,
			stancePlaintiff,
			stanceDefendant,
			summaryAi,
			judgement,
			faultRatePlaintiff,
			faultRateDefendant,
			originType,
			category);
	}
}
