package aimo.backend.domains.member.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.security.filter.jwtFilter.JwtTokenProvider;
import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.email.dto.parameter.VerifyEmailCodeParameter;
import aimo.backend.domains.email.service.EmailService;
import aimo.backend.domains.member.dto.parameter.DeleteMemberContentsParameter;
import aimo.backend.domains.member.dto.parameter.DeleteMemberParameter;
import aimo.backend.domains.member.dto.parameter.FindMyInfoParameter;
import aimo.backend.domains.member.dto.parameter.SignUpParameter;
import aimo.backend.domains.member.dto.parameter.UpdateNicknameParameter;
import aimo.backend.domains.member.dto.parameter.UpdatePasswordParameter;
import aimo.backend.domains.member.dto.request.CheckNicknameExistsRequest;
import aimo.backend.domains.member.dto.request.LogoutRequest;
import aimo.backend.domains.member.dto.request.SendNewPasswordRequest;
import aimo.backend.domains.member.dto.response.FindMyInfoResponse;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.Provider;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	// 이메일로 멤버 조회
	public Optional<Member> findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	// 회원 가입
	@Transactional(rollbackFor = ApiException.class)
	public void signUp(SignUpParameter parameter) {
		validateDuplicateEmail(parameter.email());
		validateDuplicateNickname(parameter.nickname());

		// 인증 토큰 확인
		emailService.verifyEmailCode(
			new VerifyEmailCodeParameter(parameter.email(), parameter.code()));

		String encodedPassword = passwordEncoder.encode(parameter.password());
		Member member = Member.createStandardMember(parameter, encodedPassword);
		memberRepository.save(member);
	}

	//로그아웃
	@Transactional(rollbackFor = ApiException.class)
	public void logoutMember(LogoutRequest request) {
		String accessToken = request.accessToken();

		jwtTokenProvider.expireTokens(accessToken);
	}

	// 회원 삭제
	@Transactional(rollbackFor = ApiException.class)
	public void deleteMember(DeleteMemberParameter deleteMemberParameter) {
		Member member = findMemberById(deleteMemberParameter.memberId());

		checkPassword(deleteMemberParameter.password(), member.getPassword());

		// 멤버 컨텐츠 삭제
		deleteMemberContents(DeleteMemberContentsParameter.from(member.getId()));
		memberRepository.delete(member);
	}

	// 멤버 컨텐츠 소프트 삭제
	private void deleteMemberContents(DeleteMemberContentsParameter parameter) {
		Member member = findMemberById(parameter.memberId());

		// 개인 글은 cascade로 삭제

		// 포스트 이름 삭제
		member.getPosts()
			.forEach(Post::softDelete);
		// 부모 댓글 이름 삭제
		member.getParentComments()
			.forEach(ParentComment::deleteParentCommentSoftly);
		// 자식 댓글 이름 삭제
		member.getChildComments()
			.forEach(ChildComment::deleteChildCommentSoftly);
	}

	// 비밀번호 변경
	@Transactional(rollbackFor = ApiException.class)
	public void updatePassword(UpdatePasswordParameter parameter) {
		Member member = findMemberById(parameter.memberId());

		checkPassword(parameter.password(), member.getPassword());

		member.updatePassword(passwordEncoder.encode(parameter.newPassword()));
	}

	// 닉네임 변경
	@Transactional(rollbackFor = ApiException.class)
	public void updateNickname(UpdateNicknameParameter parameter) {
		Member member = findMemberById(parameter.memberId());

		// 닉네임이 같으면 변경하지 않음
		if (member.getNickname().equals(parameter.newNickname())) {
			return;
		}

		// 닉네임 중복 검사
		validateDuplicateNickname(parameter.newNickname());

		member.updateNickname(parameter.newNickname());
	}

	// 내 정보 조회
	public FindMyInfoResponse findMyInfo(FindMyInfoParameter parameter) {
		return FindMyInfoResponse.from(findMemberById(parameter.memberId()));
	}

	// id 로 멤버 조회
	private Member findMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));
	}

	// 닉네임 중복 검사
	public boolean isNicknameExists(CheckNicknameExistsRequest request) {
		return memberRepository.existsByNickname(request.nickname());
	}

	// 비밀번호 확인
	private void checkPassword(String password, String encodedPassword) {
		boolean matches = passwordEncoder.matches(password, encodedPassword);

		if (!matches) {
			throw ApiException.from(ErrorCode.INVALID_PASSWORD);
		}
	}

	// 이메일 중복 검사
	private void validateDuplicateEmail(String email) {
		if (memberRepository.existsByEmailAndProvider(email, Provider.AIMO))
			throw ApiException.from(ErrorCode.EMAIL_DUPLICATE);
	}

	// 닉네임 중복 검사
	private void validateDuplicateNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname))
			throw ApiException.from(ErrorCode.MEMBER_NAME_DUPLICATE);
	}

	// 비밀번호 재발급
	@Transactional(rollbackFor = ApiException.class)
	public void validateCodeAndSendNewPassword(SendNewPasswordRequest request) {
		String email = request.getEmail();

		// 토큰 검증 및 삭제
		emailService.verifyEmailCode(new VerifyEmailCodeParameter(email, request.getCode()));

		// 맞는 게 있다면 그 member 비밀번호 변경 및 전송
		Member member = memberRepository.findByEmailAndProvider(email, Provider.AIMO)
			.orElseThrow(() -> ApiException.from(ErrorCode.UNAUTHENTICATED_EMAIL));

		// 비밀번호 변경
		String newPassword = createNewPassword();
		member.updatePassword(passwordEncoder.encode(newPassword));

		// 새 비밀번호 이메일 전송
		emailService.sendPasswordReissueEmail(email, newPassword);
	}

	// 새 비밀번호 생성
	private String createNewPassword() {
		// UUID 생성
		String uuid = UUID.randomUUID().toString().replace("-", "");

		// 첫 6자 추출
		return uuid.substring(0, 6);
	}
}
