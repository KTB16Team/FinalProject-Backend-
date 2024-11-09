package aimo.backend.domains.comment.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentConstants {

	UNKNOWN_MEMBER("알 수 없음"),
	DELETED_COMMENT("댓글이 삭제되었습니다."),
	DELETED_MEMBER("탈퇴한 회원"),
	;

	private final String value;
}
