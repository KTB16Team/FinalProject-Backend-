package aimo.backend.domains.email.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.email.dto.request.SendVerificationCodeRequest;
import aimo.backend.domains.email.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "EMAIL API", description = "이메일에 대한 API입니다.")
@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;

	@PostMapping("/verification-request/signup")
	@Operation(
		summary = "회원가입 email 인증 요청",
		description = "email 인증을 위한 이메일을 전송",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
			@ApiResponse(
				responseCode = "409",
				description = "이미 가입된 이메일입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			)
		}
	)
	public ResponseEntity<DataResponse<Void>> sendSignupVerificationCode(
		@RequestBody @Valid SendVerificationCodeRequest request
	) {
		emailService.validateDuplicatedEmailAndSendEmailVerification(request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/verification-request/password-reissue")
	@Operation(
		summary = "비밀번호 재발급 email 인증 요청",
		description = "email 인증을 위한 이메일을 전송",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			)
		}
	)
	public ResponseEntity<DataResponse<Void>> sendPasswordReissueVerificationCode(
		@RequestBody @Valid SendVerificationCodeRequest request
	) {
		emailService.validateEmailExistenceAndSendEmailVerification(request);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
