package aimo.backend.domains.member.service;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.domains.member.dto.DeleteRequest;
import aimo.backend.domains.member.dto.FindMyInfoResponse;
import aimo.backend.domains.member.dto.LogOutRequest;
import aimo.backend.domains.member.dto.SendTemporaryPasswordRequest;
import aimo.backend.domains.member.dto.SignUpRequest;
import aimo.backend.domains.member.dto.UpdateNicknameRequest;
import aimo.backend.domains.member.dto.UpdatePasswordRequest;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.entity.ProfileImage;
import aimo.backend.domains.member.entity.RefreshToken;
import aimo.backend.common.mapper.MemberMapper;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.member.repository.ProfileImageRepository;
import aimo.backend.domains.privatePost.dto.CreateResourceUrl;
import aimo.backend.infrastructure.s3.S3Service;
import aimo.backend.infrastructure.s3.dto.SaveFileMetaDataRequest;
import aimo.backend.infrastructure.s3.model.PresignedUrlPrefix;
import aimo.backend.infrastructure.smtp.MailService;
import aimo.backend.util.memberLoader.MemberLoader;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	private final RefreshTokenService refreshTokenService;
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	private final MemberLoader memberLoader;
	private final ProfileImageRepository profileImageRepository;
	private final S3Service s3Service;
	private final MailService mailService;

	// 이메일로 멤버 조회
	public Optional<Member> findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	// 회원 가입
	@Transactional(rollbackFor = ApiException.class)
	public void signUp(SignUpRequest signUpRequest) {
		// 중복 이메일 검사
		validateDuplicateEmail(signUpRequest.email());

		// 중복 닉네임 검사
		validateDuplicateNickname(signUpRequest.nickname());

		Member member = memberMapper.signUpMemberEntity(signUpRequest);
		memberRepository.save(member);
	}

	//로그아웃
	@Transactional(rollbackFor = ApiException.class)
	public void logoutMember(LogOutRequest logOutRequest) {
		// 회원의 refreshToken 만료 처리
		String accessToken = logOutRequest.accessToken(), refreshToken = logOutRequest.refreshToken();
		RefreshToken expiredToken = new RefreshToken(accessToken, refreshToken);
		refreshTokenService.save(expiredToken);
		log.info("Logout successful {}", refreshTokenService.existsByAccessToken(accessToken));
	}

	// 회원 삭제
	@Transactional(rollbackFor = ApiException.class)
	public void deleteMember(DeleteRequest deleteRequest) {
		Member member = memberLoader.getMember();

		if (!isValid(deleteRequest.password(), member.getPassword())) {
			throw ApiException.from(ErrorCode.INVALID_PASSWORD);
		}

		memberRepository.delete(member);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void saveProfileImageMetaData(SaveFileMetaDataRequest request) {
		Member member = memberLoader.getMember();

		if (member.getProfileImage() != null) {
			deleteProfileImage();
		}

		CreateResourceUrl createResourceUrl = new CreateResourceUrl(PresignedUrlPrefix.IMAGE.getValue(),
			request.filename(), request.extension());

		ProfileImage profileImage = ProfileImage.builder()
			.member(memberLoader.getMember())
			.filename(request.filename())
			.size(request.size())
			.extension(request.extension())
			.url(s3Service.getResourceUrl(createResourceUrl))
			.build();

		profileImageRepository.save(profileImage);
		member.updateProfileImage(profileImage);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void deleteProfileImage() {
		Member member = memberLoader.getMember();
		ProfileImage profileImage = member.getProfileImage();
		profileImageRepository.delete(profileImage);
		member.updateProfileImage(null);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
		Member member = memberLoader.getMember();

		if (!isValid(updatePasswordRequest.password(), member.getPassword())) {
			throw ApiException.from(ErrorCode.INVALID_PASSWORD);
		}

		member.updatePassword(passwordEncoder.encode(updatePasswordRequest.newPassword()));
	}

	@Transactional(rollbackFor = ApiException.class)
	public void updateNickname(UpdateNicknameRequest updateNicknameRequest) {
		Member member = memberLoader.getMember();
		validateDuplicateNickname(updateNicknameRequest.newNickname());
		member.updateNickname(updateNicknameRequest.newNickname());
	}

	public FindMyInfoResponse findMyInfo() {
		return memberMapper.toFindMyInfoResponse(memberLoader.getMember());
	}

	public Member findById(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));
	}

	public boolean checkNicknameExists(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}

	protected boolean isValid(String password, String encodedPassword) {
		return passwordEncoder.matches(password, encodedPassword);
	}

	// 이메일 중복 검사
	private void validateDuplicateEmail(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw ApiException.from(ErrorCode.EMAIL_DUPLICATE);
		}
	}

	// 닉네임 중복 검사
	private void validateDuplicateNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw ApiException.from(ErrorCode.MEMBER_NAME_DUPLICATE);
		}
	}

	@Transactional(rollbackFor = ApiException.class)
	public void updateTemporaryPasswordAndSendMail(SendTemporaryPasswordRequest sendTemporaryPasswordRequest) throws
		MessagingException {
		String temporaryPassword = UUID.randomUUID().toString().substring(0, 8);

		Member member = memberRepository.findByEmail(sendTemporaryPasswordRequest.email())
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		member.updatePassword(passwordEncoder.encode(temporaryPassword));
		mailService.sendMail(mailService.createMail(sendTemporaryPasswordRequest));
	}
}
