package aimo.backend.domains.like.dto.parameter;

import aimo.backend.domains.like.model.LikeType;

public record LikeParentCommentParameter(
	Long memberId,
	Long parentCommentId,
	LikeType likeType
) {

	public static LikeParentCommentParameter of(Long memberId, Long parentCommentId, LikeType likeType) {
		return new LikeParentCommentParameter(memberId, parentCommentId, likeType);
	}
}
