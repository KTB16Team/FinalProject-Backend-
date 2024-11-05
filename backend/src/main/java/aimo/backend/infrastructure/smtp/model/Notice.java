package aimo.backend.infrastructure.smtp.model;

import lombok.Getter;

@Getter
public enum Notice {
	TITLE("Aimo 임시 비밀번호 발급 안내"),
	INTRODUCE("임시 비밀번호 발급 안내입니다.\n"),
	TEMPORARY_PASSWORD("님의 임시 비밀번호는 "),
	END_MESSAGE("입니다.\n항상 Aimo를 이용해 주셔서 감사합니다.\n"),
	FROM("\nAimo 개발자 mango 드림"),
	SERVICE_ADDRESS("aimoservice16@gmail.com");

	private String value;

	Notice(String value) {
		this.value = value;
	}
}
