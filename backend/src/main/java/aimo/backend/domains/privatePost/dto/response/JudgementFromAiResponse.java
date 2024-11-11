package aimo.backend.domains.privatePost.dto.response;

import jakarta.validation.constraints.NotNull;

public record JudgementFromAiResponse(
	@NotNull(message = "글 제목이 비었습니다.")
	String title,
	@NotNull(message = "원고가 비었습니다.")
	String stancePlaintiff,
	@NotNull(message = "피고가 비었습니다.")
	String stanceDefendant,
	@NotNull(message = "요약이 비었습니다.")
	String summaryAi,
	@NotNull(message = "판결문이 비었습니다.")
	String judgement,
	@NotNull(message = "과실 비율이 비었습니다.")
	Double faultRate
) {

	public static JudgementFromAiResponse of(
		String title,
		String stancePlaintiff,
		String stanceDefendant,
		String summaryAi,
		String judgement,
		Double faultRate
	) {
		return new JudgementFromAiResponse(title, stancePlaintiff, stanceDefendant, summaryAi, judgement, faultRate);
	}
}
