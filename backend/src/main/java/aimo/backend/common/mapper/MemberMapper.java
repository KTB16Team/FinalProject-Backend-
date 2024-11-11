package aimo.backend.common.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.member.dto.parameter.UpdateNicknameParameter;
import aimo.backend.domains.member.dto.parameter.DeleteMemberParameter;
import aimo.backend.domains.member.dto.parameter.FindMyInfoParameter;
import aimo.backend.domains.member.dto.parameter.SignUpParameter;
import aimo.backend.domains.member.dto.parameter.UpdatePasswordParameter;
import aimo.backend.domains.member.dto.request.DeleteMemberRequest;
import aimo.backend.domains.member.dto.request.UpdateNicknameRequest;
import aimo.backend.domains.member.dto.request.UpdatePasswordRequest;
import aimo.backend.domains.member.dto.response.FindMyInfoResponse;
import aimo.backend.domains.member.dto.request.SignUpRequest;
import aimo.backend.domains.member.dto.response.NicknameExistsResponse;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.MemberRole;
import aimo.backend.domains.member.model.Provider;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberMapper {

	private final PasswordEncoder passwordEncoder;


	public static FindMyInfoResponse toFindMyInfoResponse(Member member) {
		return new FindMyInfoResponse(
			member.getNickname(),
			member.getEmail(),
			member.getProfileImage() == null ? null : member.getProfileImage().getUrl()
		);
	}

	public static DeleteMemberParameter toDeleteMemberParameter(DeleteMemberRequest deleteMemberRequest) {
		Long memberId = MemberLoader.getMemberId();
		return new DeleteMemberParameter(memberId, deleteMemberRequest.password());
	}

	public static UpdatePasswordParameter toUpdatePasswordParameter(UpdatePasswordRequest updatePasswordRequest) {
		Long memberId = MemberLoader.getMemberId();
		return new UpdatePasswordParameter(memberId, updatePasswordRequest.password(), updatePasswordRequest.newPassword());
	}

	public static UpdateNicknameParameter toUpdateNicknameParameter(UpdateNicknameRequest updateNicknameRequest) {
		Long memberId = MemberLoader.getMemberId();
		return new UpdateNicknameParameter(memberId, updateNicknameRequest.newNickname());
	}

	public static NicknameExistsResponse toNicknameExistsResponse(boolean exists) {
		return new NicknameExistsResponse(exists);
	}

	public static FindMyInfoParameter toFindMyInfoParameter() {
		Long memberId = MemberLoader.getMemberId();
		return new FindMyInfoParameter(memberId);
	}
}
