package aimo.backend.domains.vote.dto;

import aimo.backend.domains.vote.model.Side;

public record SaveVotePostParameter(
	Long memberId,
	Long postId,
	Side side
) {

	public static SaveVotePostParameter of(Long memberId, Long postId, Side side) {
		return new SaveVotePostParameter(memberId, postId, side);
	}
}
