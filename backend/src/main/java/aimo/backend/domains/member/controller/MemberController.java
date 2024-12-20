package aimo.backend.domains.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.security.filter.jwtFilter.JwtTokenProvider;
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.member.dto.parameter.DeleteMemberParameter;
import aimo.backend.domains.member.dto.parameter.FindMyInfoParameter;
import aimo.backend.domains.member.dto.parameter.SignUpParameter;
import aimo.backend.domains.member.dto.parameter.UpdateNicknameParameter;
import aimo.backend.domains.member.dto.parameter.UpdatePasswordParameter;
import aimo.backend.domains.member.dto.request.DeleteMemberRequest;
import aimo.backend.domains.member.dto.request.LogoutRequest;
import aimo.backend.domains.member.dto.request.SendNewPasswordRequest;
import aimo.backend.domains.member.dto.request.SignUpRequest;
import aimo.backend.domains.member.dto.request.UpdateNicknameRequest;
import aimo.backend.domains.member.dto.request.UpdatePasswordRequest;
import aimo.backend.domains.member.dto.response.FindMyInfoResponse;
import aimo.backend.domains.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;

	// 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<DataResponse<Void>> logoutMember(HttpServletRequest request) {
		String accessToken = jwtTokenProvider.extractAccessToken(request)
			.orElse(null);

		memberService.logoutMember(new LogoutRequest(accessToken));

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<DataResponse<Void>> signupMember(@RequestBody @Valid SignUpRequest request) {
		SignUpParameter parameter = SignUpParameter.from(request);

		memberService.signUp(parameter);

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	// 회원탈퇴
	@PostMapping
	public ResponseEntity<DataResponse<Void>> deleteMember(@RequestBody @Valid DeleteMemberRequest request) {
		Long memberId = MemberLoader.getMemberId();

		DeleteMemberParameter parameter = DeleteMemberParameter.of(memberId, request.password());

		memberService.deleteMember(parameter);

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<DataResponse<Void>> loginMember(
		@RequestParam("email") String email,
		@RequestParam("password") String password
	) {
		// 실제로 실행되지 않고 Swagger 문서용도로만 사용됩니다.
		return ResponseEntity.ok(DataResponse.ok());
	}

	// 내 정보 조회 (프로필 이미지 포함)
	@GetMapping
	public ResponseEntity<DataResponse<FindMyInfoResponse>> findMyInfo() {
		Long memberId = MemberLoader.getMemberId();

		FindMyInfoResponse response = memberService.findMyInfo(FindMyInfoParameter.from(memberId));

		return ResponseEntity.ok(DataResponse.from(response));
	}

	// 비밀번호 수정(현재 비밀번호 확인)
	@PutMapping("/password")
	public ResponseEntity<DataResponse<Void>> updatePassword(
		@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest
	) {
		Long memberId = MemberLoader.getMemberId();

		UpdatePasswordParameter parameter = UpdatePasswordParameter.of(memberId, updatePasswordRequest);

		memberService.updatePassword(parameter);

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@PostMapping("/password-reissue")
	@Operation(
		summary = "비밀번호 재발급",
		description = """
			인증 요청을 한 email이 없으면 401
			code가 일치하면 true, 일치하지 않으면 false를 반환한다.""",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
			@ApiResponse(
				responseCode = "400",
				description = "유효하지 않은 이메일 코드입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "401",
				description = "이메일 인증을 시도해주세요.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			)
		}
	)
	public ResponseEntity<DataResponse<Void>> sendNewPassword(
		@RequestBody @Valid SendNewPasswordRequest request
	) {
		memberService.validateCodeAndSendNewPassword(request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	// 닉네임 수정
	@PutMapping("/nickname")
	public ResponseEntity<DataResponse<Void>> updateNickname(
		@RequestBody @Valid UpdateNicknameRequest updateNicknameRequest
	) {
		Long memberId = MemberLoader.getMemberId();

		UpdateNicknameParameter parameter = UpdateNicknameParameter.of(memberId, updateNicknameRequest);
		memberService.updateNickname(parameter);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}
}
