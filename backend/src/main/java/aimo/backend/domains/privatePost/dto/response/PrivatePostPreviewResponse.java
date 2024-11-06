package aimo.backend.domains.privatePost.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.validation.constraints.NotNull;

public record PrivatePostPreviewResponse(
	@NotNull(message = "ID가 비었습니다.")
	Long privatePostId,

	@NotNull(message = "제목이 비었습니다.")
	String title,

	@NotNull(message = "preview가 비었습니다.")
	String contentPreview,

	@NotNull(message = "원본 타입이 비었습니다.")
	OriginType originType,

	@NotNull(message = "생성일자가 비었습니다.")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdAt,

	@NotNull(message = "발행이 비었습니다.")
	Boolean published
) {
}
