package aimo.backend.domains.like.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LikeType {
	LIKE("좋아요"),
	CANCEL_LIKE("좋아요 취소")
	;

	private final String value;
}
