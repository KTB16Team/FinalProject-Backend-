package aimo.backend.domains.member.dto.parameter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import aimo.backend.domains.member.dto.request.SignUpRequest;
import aimo.backend.domains.member.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SignUpParameter(
	@NotBlank(message = "닉네임이 필요합니다.")
	String nickname,
	@Email(message = "이메일 형식이 아닙니다.")
	String email,
	@NotBlank(message = "비밀번호가 필요합니다.")
	@Min(value = 6, message = "비밀번호는 6자 이상이어야 합니다.")
	String password,
	Gender gender,
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate birth
) {

	public static SignUpParameter from(SignUpRequest request) {
		return new SignUpParameter(
			request.nickname(),
			request.email(),
			request.password(),
			request.gender(),
			request.birth()
		);
	}
}
