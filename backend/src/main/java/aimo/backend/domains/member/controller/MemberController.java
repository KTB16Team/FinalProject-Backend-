package aimo.backend.domains.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.auth.security.jwtFilter.JwtTokenProvider;
import aimo.backend.domains.member.dto.DeleteRequest;
import aimo.backend.domains.member.dto.SignUpRequest;
import aimo.backend.domains.member.service.MemberService;
import aimo.backend.util.memberLoader.MemberLoader;
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
	private final MemberLoader memberLoader;

	@PostMapping("/logout")
	public ResponseEntity<DataResponse<Void>> logoutMember(HttpServletRequest request) {
		String accessToken = jwtTokenProvider.extractAccessToken(request).orElse(null);
		String refreshToken = jwtTokenProvider.extractRefreshToken(request).orElse(null);
		log.info("logout member access token: {} refresh token: {}", accessToken, refreshToken);
		memberService.logoutMember(accessToken, refreshToken);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/signup")
	public ResponseEntity<DataResponse<Void>> signupMember(@RequestBody @Valid SignUpRequest signUpRequest) {
		memberService.signUp(signUpRequest);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/delete")
	public ResponseEntity<DataResponse<Void>> deleteMember(
		@RequestHeader("Authorization") String accessToken,
		@RequestBody DeleteRequest deleteRequest) {
		memberService.deleteMember(accessToken, deleteRequest);

		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body(DataResponse.noContent());
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<DataResponse<Void>> loginMember(
		@RequestParam("email") String email,
		@RequestParam("password") String password
	) {
		// 실제로 실행되지 않고 Swagger 문서용도로만 사용됩니다.
		return ResponseEntity.ok(DataResponse.ok());
	}
}
