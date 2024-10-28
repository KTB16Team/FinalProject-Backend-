package aiin.backend.util.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class 	PasswordEncoderUtil {

	private final PasswordEncoder passwordEncoder;

	// 패스워드 인코딩
	public String encodePassword(String rawPw) {
		return passwordEncoder.encode(rawPw);
	}
}
