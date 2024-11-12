package aimo.backend.domains.privatePost.dto.response;
import java.time.LocalDateTime;

import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.privatePost.model.OriginType;

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
		return new PrivatePostPreviewResponse(
			privatePostId, title,
			privatePostPreview,
			originType, createdAt,
			published
		);
	}

	public static PrivatePostPreviewResponse from(PrivatePost privatePost) {
		return new PrivatePostPreviewResponse(
			privatePost.getId(),
			privatePost.getTitle(),
			privatePost.getPreview(),
			privatePost.getOriginType(),
			privatePost.getCreatedAt(),
			privatePost.getPublished()
		);
	}
}
