package aiin.backend.domains.member.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import aiin.backend.domains.member.dto.SignUpRequest;
import aiin.backend.domains.member.entity.Member;
import aiin.backend.domains.member.model.MemberRole;
import aiin.backend.domains.member.model.Provider;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberMapper {
	private final PasswordEncoder passwordEncoder;

	public Member signUpMemberEntity(SignUpRequest signUpRequest){
		return Member
			.builder()
			.username(signUpRequest.username())
			.password(passwordEncoder.encode(signUpRequest.password()))
			.email(signUpRequest.email())
			.memberRole(MemberRole.USER)
			.gender(signUpRequest.gender())
			.provider(Provider.AIMO)
			.phoneNumber(signUpRequest.phoneNumber())
			.birthDate(signUpRequest.birth())
			.build();
	}
}
