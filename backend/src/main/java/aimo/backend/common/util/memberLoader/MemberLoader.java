package aimo.backend.common.util.memberLoader;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import aimo.backend.domains.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberLoader {

	public static Long getMemberId() {
		return (Long)SecurityContextHolder
			.getContext()
			.getAuthentication()
			.getPrincipal();
	}
}
