package aimo.backend.domains.upload.dto.request;

import jakarta.validation.constraints.NotNull;

public record SaveAudioMetaDataRequest(
	@NotNull(message = "URL이 필요합니다.")
	String url,
	@NotNull(message = "파일 사이즈가 필요합니다.")
	Long size,
	@NotNull(message = "파일 이름이 필요합니다.")
	String filename,
	@NotNull(message = "파일 확장자가 필요합니다.")
	String extension
) {

	public static SaveAudioMetaDataRequest of(String url, Long size, String filename, String extension) {
		return new SaveAudioMetaDataRequest(url, size, filename, extension);
	}
}
