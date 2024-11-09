package aimo.backend.domains.member.dto.request;

import java.time.LocalDate;

import aimo.backend.domains.member.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
	@NotBlank(message = "nickname이 빈 문자열입니다.")
	String nickname,
	@Email(message = "email 형식이 아닙니다.")
	String email,
	@NotBlank(message = "password가 빈 문자열입니다.")
	@Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
	String password,
  	@NotNull(message = "gender가 비었습니다.")
	Gender gender,
  	@Past(message = "생년월일은 과거 날짜여야 합니다.")
	LocalDate birth
) {}
