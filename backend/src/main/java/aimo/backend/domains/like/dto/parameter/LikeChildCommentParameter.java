package aimo.backend.domains.like.dto.parameter;

import aimo.backend.domains.like.model.LikeType;

public record LikeChildCommentParameter(
	Long memberId,
	Long childCommentId,
	LikeType likeType
) {

	public static LikeChildCommentParameter of(Long memberId, Long childCommentId, LikeType likeType) {
		return new LikeChildCommentParameter(memberId, childCommentId, likeType);
	}
}
