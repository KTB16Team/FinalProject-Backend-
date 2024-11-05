package aimo.backend.domains.post.model;

public enum PostType {

	MY("내가 쓴 글"),
	COMMENTED("댓글 단 글"),
	POPULAR("인기 글"),
	ANY("전체 글"),
	;

	private final String value;

	PostType(String value) {
		this.value = value;
	}
}
