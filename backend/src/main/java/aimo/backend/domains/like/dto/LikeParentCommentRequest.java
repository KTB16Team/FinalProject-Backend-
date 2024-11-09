package aimo.backend.domains.like.dto;

import aimo.backend.domains.like.model.LikeType;

public record LikeParentCommentRequest(
	Long memberId,
	Long parentCommentId,
	LikeType likeType
) {
}
