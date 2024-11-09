package aimo.backend.domains.like.dto;

import aimo.backend.domains.like.model.LikeType;

public record LikeChildCommentRequest(
	Long memberId,
	Long childCommentId,
	LikeType likeType
) { }
