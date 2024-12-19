package aimo.backend.domains.email.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VerifyEmailCodeRequest {

	@NotBlank(message = "email은 비어있을 수 없습니다.")
	@Email(message = "email 형식이 올바르지 않습니다.")
	private String email;

	@NotNull(message = "code는 비어있을 수 없습니다.")
	private Integer code;
}
