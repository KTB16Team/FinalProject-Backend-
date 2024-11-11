package aimo.backend.domains.privatePost.dto.parameter;

import aimo.backend.domains.privatePost.dto.request.DeletePrivatePostRequest;

public record DeletePrivatePostParameter(
	Long memberId,
	Long privatePostId
) {

	public static DeletePrivatePostParameter of(Long memberId, Long privatePostId) {
		return new DeletePrivatePostParameter(memberId, privatePostId);
	}

	public static DeletePrivatePostParameter from(Long memberId, DeletePrivatePostRequest request) {
		return new DeletePrivatePostParameter(memberId, request.privatePostId());
	}
}
