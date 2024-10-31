package aimo.backend.domains.privatePost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public record UploadAudioSuccessRequest(
	@NotNull(message = "Audio file url is required")
	String url,
	@NotNull(message = "Audio file size is required")
	Long size,
	@JsonProperty("filename")
	@NotNull(message = "Audio file file name is required")
	String filename,
	@JsonProperty("extension")
	String extension
) {
}
