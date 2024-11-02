package aimo.backend.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	//Common
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "COMMON-001"),
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 파라미터가 잘못 되었습니다.", "COMMON-002"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 에러가 발생하였습니다.", "COMMON-003"),
	EMAIL_BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "이메일 전송에 실패하였습니다.", "COMMON-004"),
	NULL_POINTER(HttpStatus.INTERNAL_SERVER_ERROR, "Null 포인터 Exception이 발생하였습니다.", "COMMON-005"),
	ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "IllegalArgument Exception이 발생하였습니다.", "COMMON-006"),
	IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Stream 변환 과정에서 에러가 발생하였습니다.", "COMMON-007"),

	//Auth
	INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다.", "AUTH-001"),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.", "AUTH-002"),
	REISSUE_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰 재발급이 필요합니다.", "AUTH-003"),
	EMAIL_AUTHENTICATION_FAIL(HttpStatus.BAD_REQUEST, "이메일 인증에 실패하였습니다.", "AUTH-004"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다.", "AUTH-005"),

	//Member
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 잘못 되었습니다.", "MEMBER-001"),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 회원을 찾을 수 없습니다.", "MEMBER-002"),
	EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.", "MEMBER-003"),
	USERNAME_DUPLICATE(HttpStatus.CONFLICT, "이미 가입된 닉네임입니다.", "MEMBER-004"),
	USERNAME_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "닉네임 변경에 실패하였습니다.", "MEMBER-005"),
	PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 프로필 이미지를 찾을 수 없습니다.", "MEMBER-006"),
	MAIL_SEND_FAIL(HttpStatus.BAD_REQUEST, "이메일 전송에 실패하였습니다.", "MEMBER-008"),
	INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일 형식이 잘못 되었습니다.", "MEMBER-009"),
	EMAIL_NOT_MATCH(HttpStatus.BAD_REQUEST, "이메일이 일치하지 않습니다.", "MEMBER-010"),

	//PrivatePost
	PRIVATE_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 대화록을 찾을 수 없습니다.", "DISPUTE-001"),
	PRIVATE_POST_CREATE_FAIL(HttpStatus.BAD_REQUEST, "대화록 생성에 실패하였습니다.", "DISPUTE-002"),
	PRIVATE_POST_DELETE_FAIL(HttpStatus.BAD_REQUEST, "대화록 삭제에 실패하였습니다.", "DISPUTE-003"),
	PRIVATE_POST_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "대화록 수정에 실패하였습니다.", "DISPUTE-004"),
	PRIVATE_POST_DELETE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "대화록 삭제 권한이 없습니다.", "DISPUTE-006"),
	PRIVATE_POST_CREATE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "대화록 생성 권한이 없습니다.", "DISPUTE-007"),
	PRIVATE_POST_UPDATE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "대화록 수정 권한이 없습니다.", "DISPUTE-007"),

	//Post
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 게시글을 찾을 수 없습니다.", "POST-001"),
	POST_DELETE_FAIL(HttpStatus.BAD_REQUEST, "게시글 삭제에 실패하였습니다.", "POST-002"),
	POST_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "게시글 수정에 실패하였습니다.", "POST-003"),
	POST_CREATE_FAIL(HttpStatus.BAD_REQUEST, "게시글 작성에 실패하였습니다.", "POST-004"),
	POST_LIKE_FAIL(HttpStatus.BAD_REQUEST, "게시글 좋아요에 실패하였습니다.", "POST-005"),
	POST_VOTE_FAIL(HttpStatus.BAD_REQUEST, "게시글 투표에 실패하였습니다.", "POST-006"),
	POST_DELETE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "게시글 삭제 권한이 없습니다.", "POST-007"),
	POST_UPDATE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "게시글 수정 권한이 없습니다.", "POST-008"),
	POST_CREATE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "게시글 작성 권한이 없습니다.", "POST-009"),
	POST_VOTE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "게시글 투표 권한이 없습니다.", "POST-010"),
	POST_LIKE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "게시글 좋아요 권한이 없습니다.", "POST-011"),

	//ParentComment
	PARENT_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 부모 댓글을 찾을 수 없습니다.", "PARENT_COMMENT-001"),
	UNAUTHORIZED_PARENT_COMMENT(HttpStatus.FORBIDDEN, "부모 댓글 작성자만 수정, 삭제가 가능합니다.", "PARENT-COMMENT-002"),

	//ChildComment
	CHILD_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 자식 댓글을 찾을 수 없습니다.", "CHILD_COMMENT-001"),
	UNAUTHORIZED_CHILD_COMMENT(HttpStatus.FORBIDDEN, "자식 댓글 작성자만 수정, 삭제가 가능합니다.", "CHILD-COMMENT-002"),

	//AI
	AI_BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "AI 서버와의 통신에 실패하였습니다.", "AI-001"),
	AI_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI 서버 내부에서 에러가 발생하였습니다.", "AI-002"),

	// FILE
	INVALID_PREFIX(HttpStatus.BAD_REQUEST, "잘못된 파일 경로입니다.", "FILE-000"),
	PREFIX_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 경로를 찾을 수 없습니다.", "FILE-001"),
	PREFIX_IS_NULL(HttpStatus.BAD_REQUEST, "파일 경로가 비어있습니다.", "FILE-002"),
	INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "파일 이름이 잘못 되었습니다.", "FILE-003"),
	INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "파일 확장자가 잘못 되었습니다.", "FILE-004"),

	;
	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
