package aimo.backend.domains.member.service;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.domains.auth.security.jwtFilter.JwtTokenProvider;
import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.dto.parameter.UpdateNicknameParameter;
import aimo.backend.domains.member.dto.parameter.DeleteMemberContentsParameter;
import aimo.backend.domains.member.dto.parameter.DeleteMemberParameter;
import aimo.backend.domains.member.dto.parameter.DeleteProfileImageParameter;
import aimo.backend.domains.member.dto.parameter.FindMyInfoParameter;
import aimo.backend.domains.member.dto.parameter.SaveFileMetaDataParameter;
import aimo.backend.domains.member.dto.parameter.SignUpParameter;
import aimo.backend.domains.member.dto.parameter.UpdatePasswordParameter;
import aimo.backend.domains.member.dto.request.CheckNicknameExistsRequest;
import aimo.backend.domains.member.dto.response.FindMyInfoResponse;
import aimo.backend.domains.member.dto.request.LogoutRequest;
import aimo.backend.domains.member.dto.request.SendTemporaryPasswordRequest;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.entity.ProfileImage;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.member.repository.ProfileImageRepository;
import aimo.backend.domains.post.dto.parameter.SoftDeletePostParameter;
import aimo.backend.domains.post.service.PostService;
import aimo.backend.infrastructure.s3.dto.parameter.CreateResourceUrlParameter;
import aimo.backend.infrastructure.s3.S3Service;
import aimo.backend.infrastructure.s3.model.PresignedUrlPrefix;
import aimo.backend.infrastructure.smtp.MailService;
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
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final ProfileImageRepository profileImageRepository;
	private final S3Service s3Service;
	private final MailService mailService;
	private final PostService postService;

	// 이메일로 멤버 조회
	public Optional<Member> findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	// 회원 가입
	@Transactional(rollbackFor = ApiException.class)
	public void signUp(SignUpParameter parameter) {
		validateDuplicateEmail(parameter.email());
		validateDuplicateNickname(parameter.nickname());

		String encodedPassword = passwordEncoder.encode(parameter.password());
		Member member = Member.of(parameter, encodedPassword);
		memberRepository.save(member);
	}

	//로그아웃
	@Transactional(rollbackFor = ApiException.class)
	public void logoutMember(LogoutRequest logOutRequest) {
		String accessToken = logOutRequest.accessToken();
		jwtTokenProvider.expireTokens(accessToken);
	}

	// 회원 삭제
	@Transactional(rollbackFor = ApiException.class)
	public void deleteMember(DeleteMemberParameter deleteMemberParameter) {
		Member member = findMemberById(deleteMemberParameter.memberId());

		checkPassword(deleteMemberParameter.password(), member.getPassword());

		// 멤버 컨텐츠 삭제
		deleteMemberContents(DeleteMemberContentsParameter.of(member.getId()));
		memberRepository.delete(member);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void saveProfileImageMetaData(SaveFileMetaDataParameter parameter) {
		Long memberId = parameter.memberId();
		Member member = findMemberById(memberId);
		DeleteProfileImageParameter deleteProfileImageParameter = new DeleteProfileImageParameter(memberId);

		if (member.getProfileImage() != null)
			deleteProfileImage(deleteProfileImageParameter);
		
		CreateResourceUrlParameter createResourceUrlParameter =
			CreateResourceUrlParameter.from(PresignedUrlPrefix.IMAGE, parameter);
		String url = s3Service.getResourceUrl(createResourceUrlParameter);
		ProfileImage profileImage = ProfileImage.of(parameter, member, url);

		profileImageRepository.save(profileImage);
		member.updateProfileImage(profileImage);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void deleteProfileImage(DeleteProfileImageParameter deleteProfileImageParameter) {
		Member member = findMemberById(deleteProfileImageParameter.memberId());
		ProfileImage profileImage = member.getProfileImage();
		profileImageRepository.delete(profileImage);
		member.updateProfileImage(null);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void updatePassword(UpdatePasswordParameter updatePasswordParameter) {
		Member member = findMemberById(updatePasswordParameter.memberId());

		checkPassword(updatePasswordParameter.password(), member.getPassword());

		member.updatePassword(passwordEncoder.encode(updatePasswordParameter.newPassword()));
	}

	// 닉네임 변경
	@Transactional(rollbackFor = ApiException.class)
	public void updateNickname(UpdateNicknameParameter parameter) {
		Member member = findMemberById(parameter.memberId());
		validateDuplicateNickname(parameter.newNickname());
		member.updateNickname(parameter.newNickname());
	}

	// 내 정보 조회
	public FindMyInfoResponse findMyInfo(FindMyInfoParameter parameter) {
		return FindMyInfoResponse.from(findMemberById(parameter.memberId()));
	}

	// id 로 멤버 조회
	public Member findMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));
	}

	// 닉네임 중복 검사
	public boolean isNicknameExists(CheckNicknameExistsRequest request) {
		return memberRepository.existsByNickname(request.nickname());
	}

	// 비밀번호 확인
	protected void checkPassword(String password, String encodedPassword) {
		boolean matches = passwordEncoder.matches(password, encodedPassword);

		if (!matches) {
			throw ApiException.from(ErrorCode.INVALID_PASSWORD);
		}
	}

	// 멤버 컨텐츠 소프트 삭제
	private void deleteMemberContents(DeleteMemberContentsParameter parameter) {
		Member member = findMemberById(parameter.memberId());

		member.getPosts()
			.forEach(post -> postService.softDeleteBy(new SoftDeletePostParameter(post.getId())));
		member.getParentComments()
			.stream()
			.filter(parentComment -> !parentComment.getIsDeleted())
			.forEach(ParentComment::deleteParentCommentSoftly);

		member.getChildComments()
			.stream()
			.filter(childComment -> !childComment.getIsDeleted())
			.forEach(ChildComment::deleteChildCommentSoftly);

	}

	// 이메일 중복 검사
	private void validateDuplicateEmail(String email) {
		if (memberRepository.existsByEmail(email))
			throw ApiException.from(ErrorCode.EMAIL_DUPLICATE);
	}

	// 닉네임 중복 검사
	private void validateDuplicateNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname))
			throw ApiException.from(ErrorCode.MEMBER_NAME_DUPLICATE);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void updateTemporaryPasswordAndSendMail(
		SendTemporaryPasswordRequest sendTemporaryPasswordRequest) throws MessagingException {
		String temporaryPassword = UUID.randomUUID().toString().substring(0, 8);

		Member member = memberRepository.findByEmail(sendTemporaryPasswordRequest.email())
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		member.updatePassword(passwordEncoder.encode(temporaryPassword));
		mailService.sendMail(mailService.createMail(sendTemporaryPasswordRequest));
	}
}
