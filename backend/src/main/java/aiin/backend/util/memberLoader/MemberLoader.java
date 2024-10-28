package aiin.backend.util.memberLoader;


import static aiin.backend.common.exception.ErrorCode.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import aiin.backend.common.exception.ApiException;
import aiin.backend.domains.member.entity.Member;
import aiin.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberLoader {

	private final MemberRepository memberRepository;

	// Authentication 객체에서 Member를 찾는 메서드
	public Member getMember() {
		String email = getEmail();

		return memberRepository.findByEmail(email)
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));
	}

	// Authentication 객체에서 email을 추출하는 메서드
	public String getEmail() {
		return (String)SecurityContextHolder
			.getContext()
			.getAuthentication()
			.getPrincipal();
	}
}
