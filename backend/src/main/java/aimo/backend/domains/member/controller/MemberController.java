package aimo.backend.domains.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.domains.member.dto.CreateProfileImageUrlRequest;
import aimo.backend.domains.member.dto.DeleteRequest;
import aimo.backend.domains.member.dto.LogOutRequest;
import aimo.backend.domains.member.dto.SignUpRequest;
import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.auth.security.jwtFilter.JwtTokenProvider;
import aimo.backend.infrastructure.s3.S3Service;
import aimo.backend.infrastructure.s3.dto.CreatePresignedUrlResponse;
import aimo.backend.infrastructure.s3.dto.SaveFileMetaDataRequest;
import aimo.backend.domains.member.service.MemberService;
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
	private final S3Service s3Service;

	@PostMapping("/logout")
	public ResponseEntity<DataResponse<Void>> logoutMember(HttpServletRequest request) {
		String accessToken = jwtTokenProvider.extractAccessToken(request).orElse(null);
		String refreshToken = jwtTokenProvider.extractRefreshToken(request).orElse(null);
		log.info("logout member access token: {} refresh token: {}", accessToken, refreshToken);
		memberService.logoutMember(new LogOutRequest(accessToken, refreshToken));

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
		memberService.deleteMember(deleteRequest);

		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body(DataResponse.noContent());
	}

	@PostMapping("/members/profile/presigned")
	public ResponseEntity<DataResponse<CreatePresignedUrlResponse>> createProfileImagePreSignedUrl(
		@RequestBody CreateProfileImageUrlRequest request) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created(s3Service.createProfilePresignedUrl(request)));
	}

	@PostMapping("/members/profile/presigned/upload/success")
	public ResponseEntity<DataResponse<Void>> saveProfileImageMetaData(
		@RequestBody SaveFileMetaDataRequest request) {
		memberService.saveProfileImageMetaData(request);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}
}
