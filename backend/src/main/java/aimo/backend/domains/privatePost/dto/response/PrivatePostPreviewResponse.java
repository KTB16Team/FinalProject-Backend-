package aimo.backend.domains.privatePost.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.validation.constraints.NotNull;

public record PrivatePostPreviewResponse(
	Long privatePostId,
	String title,
	String privatePostPreview,
	OriginType originType,
	LocalDateTime createdAt,
	Boolean published
) {

	public static PrivatePostPreviewResponse of(
		Long privatePostId,
		String title,
		String privatePostPreview,
		OriginType originType,
		LocalDateTime createdAt,
		Boolean published
	) {
		return new PrivatePostPreviewResponse(privatePostId, title, privatePostPreview, originType, createdAt, published);
	}
}
