package aimo.backend.domains.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.domains.member.dto.CreateProfileImageUrlRequest;
import aimo.backend.domains.member.dto.DeleteRequest;
import aimo.backend.domains.member.dto.FindMyInfoResponse;
import aimo.backend.domains.member.dto.LogOutRequest;
import aimo.backend.domains.member.dto.NicknameExistsResponse;
import aimo.backend.domains.member.dto.SendTemporaryPasswordRequest;
import aimo.backend.domains.member.dto.SignUpRequest;
import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.auth.security.jwtFilter.JwtTokenProvider;
import aimo.backend.domains.member.dto.UpdateNicknameRequest;
import aimo.backend.domains.member.dto.UpdatePasswordRequest;
import aimo.backend.infrastructure.s3.S3Service;
import aimo.backend.infrastructure.s3.dto.CreatePresignedUrlResponse;
import aimo.backend.infrastructure.s3.dto.SaveFileMetaDataRequest;

import aimo.backend.domains.member.service.MemberService;
import jakarta.mail.MessagingException;
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
	private final S3Service s3Service;

	@PostMapping("/logout")
	public ResponseEntity<DataResponse<Void>> logoutMember(HttpServletRequest request) {
		String accessToken = jwtTokenProvider.extractAccessToken(request).orElse(null);
		String refreshToken = jwtTokenProvider.extractRefreshToken(request).orElse(null);
		log.info("logout member access token: {} refresh token: {}", accessToken, refreshToken);
		memberService.logoutMember(new LogOutRequest(accessToken, refreshToken));

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@PostMapping("/signup")
	public ResponseEntity<DataResponse<Void>> signupMember(@RequestBody @Valid SignUpRequest signUpRequest) {
		memberService.signUp(signUpRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@DeleteMapping
	public ResponseEntity<DataResponse<Void>> deleteMember(@RequestBody DeleteRequest deleteRequest) {
		memberService.deleteMember(deleteRequest);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(DataResponse.noContent());
	}

	@PostMapping("/profile/presigned")
	public ResponseEntity<DataResponse<CreatePresignedUrlResponse>> createProfileImagePreSignedUrl(
		@RequestBody CreateProfileImageUrlRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(s3Service.createProfilePresignedUrl(request)));
	}

	@PostMapping("/profile/success")
	public ResponseEntity<DataResponse<Void>> saveProfileImageMetaData(@RequestBody SaveFileMetaDataRequest request) {
		memberService.saveProfileImageMetaData(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@DeleteMapping("/profile")
	public ResponseEntity<DataResponse<Void>> deleteProfileImage() {
		memberService.deleteProfileImage();

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(DataResponse.noContent());
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<DataResponse<Void>> loginMember(@RequestParam("email") String email,
		@RequestParam("password") String password) {
		// 실제로 실행되지 않고 Swagger 문서용도로만 사용됩니다.
		return ResponseEntity.ok(DataResponse.ok());
	}

	// 내 정보 조회 (프로필 이미지 포함)
	@GetMapping
	public ResponseEntity<DataResponse<FindMyInfoResponse>> findMyInfo() {
		return ResponseEntity.status(HttpStatus.OK)
			.body(DataResponse.from(memberService.findMyInfo()));
	}

	// 비밀번호 수정(현재 비밀번호 확인)
	@PutMapping("/password")
	public ResponseEntity<DataResponse<Void>> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
		memberService.updatePassword(updatePasswordRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	// 비밀번호 재발급(임시 비밀번호를 이메일로 전송)
	@PostMapping("/password/temp")
	public ResponseEntity<DataResponse<Void>> sendTemporaryPassword(
		@Valid @RequestBody SendTemporaryPasswordRequest sendTemporaryPasswordRequest) throws MessagingException {
		memberService.updateTemporaryPasswordAndSendMail(sendTemporaryPasswordRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}


	@GetMapping("/nickname/{nickname}/exists")
	public ResponseEntity<DataResponse<NicknameExistsResponse>> checkNicknameExists(@PathVariable("nickname") String nickname) {
		NicknameExistsResponse exist = new NicknameExistsResponse(memberService.checkNicknameExists(nickname));

		return ResponseEntity.status(HttpStatus.OK).body(DataResponse.from(exist));
	}

	@PutMapping("/nickname")
	public ResponseEntity<DataResponse<Void>> updateNickname(@RequestBody UpdateNicknameRequest updateNicknameRequest) {
		memberService.updateNickname(updateNicknameRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}
}
