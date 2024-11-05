package aimo.backend.domains.privatePost.dto.response;

import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record JudgementResponse(
	String title,
	String summary,
	String stancePlaintiff,
	String stanceDefendant,
	String judgement,
	Integer faultRatePlaintiff,
	Integer faultRateDefendant,
	@Enumerated(EnumType.STRING)
	OriginType originType
) {
}
