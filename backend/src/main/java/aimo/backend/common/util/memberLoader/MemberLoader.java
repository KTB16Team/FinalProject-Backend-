package aimo.backend.common.util.memberLoader;

import org.springframework.security.core.context.SecurityContextHolder;

public class MemberLoader {

	public static Long getMemberId() {
		return (Long)SecurityContextHolder
			.getContext()
			.getAuthentication()
			.getPrincipal();
	}
}
