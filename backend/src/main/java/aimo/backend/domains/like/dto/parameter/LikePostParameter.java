package aimo.backend.domains.like.dto.parameter;

import aimo.backend.domains.like.model.LikeType;

public record LikePostParameter(
	Long memberId,
	Long postId,
	LikeType likeType
) {

	public static LikePostParameter of(Long memberId, Long postId, LikeType likeType) {
		return new LikePostParameter(memberId, postId, likeType);
	}
}
