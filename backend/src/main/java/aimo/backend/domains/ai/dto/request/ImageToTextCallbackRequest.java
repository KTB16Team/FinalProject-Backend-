package aimo.backend.domains.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ImageToTextCallbackRequest(
	@NotBlank(message = "accessKey가 필요합니다.")
	String accessKey,
	@NotNull(message = "status가 필요합니다.")
	Boolean status,

	String url,
	@NotBlank(message = "script가 필요합니다.")
	String script,
	@NotNull(message = "memberId가 필요합니다.")
	Long memberId,
	@NotNull(message = "privatePostId가 필요합니다.")
	Long privatePostId
) {
}
