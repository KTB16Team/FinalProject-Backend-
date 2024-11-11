package aimo.backend.domains.privatePost.dto.parameter;

import aimo.backend.domains.privatePost.dto.request.FindPrivatePostRequest;

public record FindPrivatePostParameter(
	Long memberId,
	Long privatePostId
) {

	public static FindPrivatePostParameter of(Long memberId, Long privatePostId) {
		return new FindPrivatePostParameter(memberId, privatePostId);
	}

	public static FindPrivatePostParameter from(Long memberId, FindPrivatePostRequest request) {
		return new FindPrivatePostParameter(memberId, request.privatePostId());
	}
}
