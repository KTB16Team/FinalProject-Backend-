package aimo.backend.domains.vote.dto;

import aimo.backend.domains.vote.model.Side;

public record SaveVotePostRequest(
	Long postId,
	Long memberId,
	Side side) {
}
