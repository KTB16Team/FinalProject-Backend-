package aimo.backend.domains.privatePost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public record SaveAudioSuccessRequest(
	@NotNull(message = "URL이 필요합니다.")
	String url,
	@NotNull(message = "파일 사이즈가 필요합니다.")
	Long size,
	@JsonProperty("filename")
	@NotNull(message = "파일 이름이 필요합니다.")
	String filename,
	@JsonProperty("extension")
	@NotNull(message = "파일 확장자가 필요합니다.")
	String extension
) { }
