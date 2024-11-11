package aimo.backend.domains.privatePost.dto.request;

import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record JudgementToAiRequest(
	@NotNull(message = "대화록이 비었습니다.")
	String content,
	@Enumerated(EnumType.STRING)
	@NotNull(message = "원본 타입이 비었습니다.")
	OriginType originType
) {

	public static JudgementToAiRequest of(String content, OriginType originType) {
		return new JudgementToAiRequest(content, originType);
	}
}
