package aimo.backend.domains.privatePost.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AudioRecordPresignedRequest(
	@NotNull(message = "파일명이 비었습니다.")
	String filename
) {
}
