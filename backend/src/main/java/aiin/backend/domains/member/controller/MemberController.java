package aiin.backend.domains.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aiin.backend.domains.member.dto.DeleteRequest;
import aiin.backend.domains.member.dto.SignUpRequest;
import aiin.backend.common.dto.DataResponse;
import aiin.backend.auth.security.jwtFilter.JwtTokenProvider;
import aiin.backend.util.memberLoader.MemberLoader;
import aiin.backend.domains.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/members")
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
	public ResponseEntity<DataResponse<Void>> signupMember(@RequestBody SignUpRequest signUpRequest) {
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
}
