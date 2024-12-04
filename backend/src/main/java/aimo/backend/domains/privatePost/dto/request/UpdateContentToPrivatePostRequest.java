package aimo.backend.domains.privatePost.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateContentToPrivatePostRequest(
	@NotNull(message = "PrivatePost ID가 필요합니다.")
	Long id,
	@NotBlank(message = "제목이 비었습니다.")
	String title,
	@NotBlank(message = "원고가 비었습니다.")
	String stancePlaintiff,
	@NotBlank(message = "피고가 비었습니다.")
	String stanceDefendant,
	@NotBlank(message = "AI 요약이 비었습니다.")
	String summaryAi,
	@NotBlank(message = "판결문이 비었습니다.")
	String judgement,
	@NotNull(message = "과실률이 비었습니다.")
	Float faultRate
) {
}
