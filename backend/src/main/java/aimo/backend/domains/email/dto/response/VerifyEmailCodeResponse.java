package aimo.backend.domains.email.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyEmailCodeResponse {

	private String token;
}