package aimo.backend.domains.email.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SendVerificationCodeRequest {

	@NotBlank(message = "email은 비어있을 수 없습니다.")
	@Email(message = "email 형식이 올바르지 않습니다.")
	private String email;
}