package aimo.backend.domains.like.dto.parameter;

import aimo.backend.domains.like.model.LikeType;

public record LikeParentCommentParameter(
	Long memberId,
	Long parentCommentId,
	LikeType likeType
) {
}
