package aimo.backend.domains.email.service;

import java.util.Objects;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.domains.email.domain.EmailCode;
import aimo.backend.domains.email.dto.parameter.VerifyEmailCodeParameter;
import aimo.backend.domains.email.dto.request.SendVerificationCodeRequest;
import aimo.backend.domains.email.repository.EmailCodeRepository;
import aimo.backend.domains.email.util.EmailUtil;
import aimo.backend.domains.member.model.Provider;
import aimo.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailService {

	private final EmailCodeRepository emailCodeRepository;
	private final MemberRepository memberRepository;
	private final EmailUtil emailUtil;

	private static final String EMAIL_TITLE = "AIMO";
	private static final String EMAIL_VERIFICATION_NOTICE_TEXT = "인증 코드는 %d 입니다.";
	private static final String EMAIL_NEW_PASSWORD_NOTICE_TEXT = "임시 비밀번호는 %s 입니다. 로그인 후 비밀번호를 변경해주세요.";

	// 회원가입 이메일 인증번호 보내기
	@Transactional(rollbackFor = ApiException.class)
	public void validateDuplicatedEmailAndSendEmailVerification(SendVerificationCodeRequest request) {
		String email = request.getEmail();

		validateDuplicatedEmail(email);

		Integer verificationCode = createVerificationCode();
		checkEmailCodeDuplicationAndSaveEmailCode(email, verificationCode);

		sendVerificationCodeEmail(email, verificationCode);
	}

	// 이메일 중복 체크
	public void validateDuplicatedEmail(String email) {
		// AIMO로 가입된 회원들한에서만 조회
		if (memberRepository.existsByEmailAndProvider(email, Provider.AIMO)) {
			throw ApiException.from(ErrorCode.EMAIL_DUPLICATE);
		}
	}

	// 비밀번호 재발급 이메일 인증번호 보내기
	@Transactional(rollbackFor = ApiException.class)
	public void validateEmailExistenceAndSendEmailVerification(SendVerificationCodeRequest request) {
		String email = request.getEmail();

		validateEmailExistence(email);

		Integer code = createVerificationCode();
		checkEmailCodeDuplicationAndSaveEmailCode(email, code);

		sendVerificationCodeEmail(email, code);
	}

	// 이메일 존재 확인
	public void validateEmailExistence(String email) {
		// AIMO로 가입된 회원들한에서만 조회
		if (!memberRepository.existsByEmailAndProvider(email, Provider.AIMO)) {
			throw ApiException.from(ErrorCode.MEMBER_NOT_FOUND);
		}
	}

	// 인증번호 이메일 보내기
	public void sendVerificationCodeEmail(String email, Integer code) {
		String text = String.format(EMAIL_VERIFICATION_NOTICE_TEXT, code);

		emailUtil.sendEmail(email, EMAIL_TITLE, text);
	}

	// 이메일 인증 확인
	@Transactional(rollbackFor = ApiException.class)
	public void verifyEmailCode(VerifyEmailCodeParameter parameter) {
		String email = parameter.getEmail();
		Integer requestCode = parameter.getCode();

		// 이메일로 인증 코드 검색
		EmailCode emailCode = emailCodeRepository.findById(email)
			.orElseThrow(() -> ApiException.from(ErrorCode.UNAUTHENTICATED_EMAIL));

		checkCode(emailCode.getCode(), requestCode);

		// 인증 후 인증 코드 삭제
		emailCodeRepository.delete(emailCode);
	}

	// 코드 비교
	public void checkCode(Integer a1, Integer a2) {
		if (!Objects.equals(a1, a2)) {
			throw ApiException.from(ErrorCode.INVALID_EMAIL_CODE);
		}

	}

	// 비밀번호 재발급 이메일 보내기
	public void sendPasswordReissueEmail(String email, String newPassword) {
		String text = String.format(EMAIL_NEW_PASSWORD_NOTICE_TEXT, newPassword);

		emailUtil.sendEmail(email, EMAIL_TITLE, text);
	}

	// 인증 코드 생성
	private Integer createVerificationCode() {
		Random random = new Random();

		return random.nextInt(900000) + 100000; // 100000부터 999999까지의 숫자 생성
	}

	// 이메일 코드 중복 확인 및 저장
	@Transactional
	public void checkEmailCodeDuplicationAndSaveEmailCode(String email, Integer code) {
		EmailCode emailCode = emailCodeRepository.findById(email).orElse(null);

		// 1. 이미 저장된 emailCode가 있으면 code만 변경
		// 2. 없으면 새로 생성
		if (emailCode != null) {
			emailCode.setCode(code);
		} else {
			emailCode = new EmailCode(email, code);
		}

		emailCodeRepository.save(emailCode);
	}
}
