package aiin.backend.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	//400
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "COMMON-001"),
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 파라미터가 잘못 되었습니다.", "COMMON-002"),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀 번호가 잘못 되었습니다.", "COMMON-002"),

	//401
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.", "COMMON-003"),
	INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다.", "MEMBER-001"),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.", "MEMBER-002"),

	//402
	REISSUE_ACCESS_TOKEN(HttpStatus.PAYMENT_REQUIRED, "액세스 토큰 재발급이 필요합니다.", "MEMBER-003"),

	//404
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 회원을 찾을 수 없습니다.", "MEMBER-004"),

	//409
	EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.", "MEMBER-005"),

	//500
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 에러가 발생하였습니다.", "COMMON-002"),

	//502
	EMAIL_BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "이메일 전송에 실패하였습니다.", "COMMON-003"),

	//Post
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 게시글을 찾을 수 없습니다.", "POST-001"),

	//ParentComment
	PARENT_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 부모 댓글을 찾을 수 없습니다.", "PARENT_COMMENT-001"),
	UNAUTHORIZED_PARENT_COMMENT(HttpStatus.FORBIDDEN, "부모 댓글 작성자만 수정, 삭제가 가능합니다.", "PARENT-COMMENT-002"),

	//ChildComment
	CHILD_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 자식 댓글을 찾을 수 없습니다.", "CHILD_COMMENT-001"),
	UNAUTHORIZED_CHILD_COMMENT(HttpStatus.FORBIDDEN, "자식 댓글 작성자만 수정, 삭제가 가능합니다.", "CHILD-COMMENT-002"),

	;
	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
