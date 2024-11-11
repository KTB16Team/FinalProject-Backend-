package aimo.backend.common.util.memberLoader;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import aimo.backend.domains.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberLoader {

	// Authentication 객체에서 email을 추출하는 메서드
	public static Long getMemberId() {
		return (Long)SecurityContextHolder
			.getContext()
			.getAuthentication()
			.getPrincipal();
	}
}
