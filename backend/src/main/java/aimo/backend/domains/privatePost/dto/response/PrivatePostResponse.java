package aimo.backend.domains.privatePost.dto.response;

import jakarta.validation.constraints.NotNull;

public record PrivatePostResponse(
	@NotNull(message = "제목이 비었습니다.")
	String title,
	@NotNull(message = "요약이 비었습니다.")
	String summaryAi,
	@NotNull(message = "원고가 비었습니다.")
	String stancePlaintiff,
	@NotNull(message = "피고가 비었습니다.")
	String stanceDefendant,
	@NotNull(message = "판단이 비었습니다.")
	String judgement,
	@NotNull(message = "판결이 비었습니다.")
	Double faultRate,
	@NotNull(message = "발행이 비었습니다.")
	Boolean published
) {
}
