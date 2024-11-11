package aimo.backend.domains.like.dto.parameter;

import aimo.backend.domains.like.model.LikeType;

public record LikeChildCommentParameter(
	Long memberId,
	Long childCommentId,
	LikeType likeType
) {
}
