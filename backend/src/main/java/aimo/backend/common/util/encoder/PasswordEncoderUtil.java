package aimo.backend.common.util.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordEncoderUtil {

	private final PasswordEncoder passwordEncoder;

	public String encodePassword(String rawPw) {
		return passwordEncoder.encode(rawPw);
	}
}
