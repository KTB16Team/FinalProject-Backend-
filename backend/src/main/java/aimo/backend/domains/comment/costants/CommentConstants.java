package aimo.backend.domains.comment.costants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentConstants {
	DELETED_COMMENT("댓글이 삭제되었습니다."),
	DELETED_MEMBER("탈퇴한 회원"),
	;

	private final String value;
}
