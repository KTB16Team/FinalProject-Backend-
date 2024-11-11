package aimo.backend.domains.post.dto.parameter;

import aimo.backend.domains.post.model.PostType;

public record FindPostByPostTypeParameter(
	Long memberId,
	PostType postType,
	Integer page,
	Integer size
) {
	public static FindPostByPostTypeParameter of(Long memberId, PostType postType, Integer page, Integer size) {
		return new FindPostByPostTypeParameter(memberId, postType, page, size);
	}
}
