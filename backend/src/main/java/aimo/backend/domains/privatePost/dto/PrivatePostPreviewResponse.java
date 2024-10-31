package aimo.backend.domains.privatePost.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import aimo.backend.domains.privatePost.model.OriginType;
import jakarta.validation.constraints.NotNull;

public record PrivatePostPreviewResponse(
	@NotNull(message = "privatePostId is null")
	@JsonProperty("private_post_id")
	Long privatePostId,

	@NotNull(message = "title is null")
	String title,

	@NotNull(message = "privatePostPreview is null")
	@JsonProperty("private_post_preview")
	String privatePostPreview,

	@NotNull(message = "privatePostPreviewType is null")
	@JsonProperty("origin_type")
	OriginType originType,

	@NotNull(message = "createdAt is null")
	@JsonProperty("created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdAt,

	@NotNull(message = "published is null")
	Boolean published
) {
}
