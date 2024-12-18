package aimo.backend.domains.email.dto.parameter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VerifyEmailCodeParameter {

	private String email;

	private Integer code;
}
