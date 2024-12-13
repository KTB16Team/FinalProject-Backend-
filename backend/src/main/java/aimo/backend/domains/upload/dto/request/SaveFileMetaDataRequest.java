package aimo.backend.domains.upload.dto.request;

import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;
import jakarta.validation.constraints.NotNull;

public record SaveFileMetaDataRequest(
	@NotNull(message = "URL이 필요합니다.")
	String url,
	@NotNull(message = "파일 사이즈가 필요합니다.")
	Long size,
	@NotNull(message = "파일 이름이 필요합니다.")
	String filename,
	@NotNull(message = "파일 확장자가 필요합니다.")
	String extension,
	@NotNull(message = "prefix가 필요합니다.")
	PreSignedUrlPrefix prefix
) {

	public static SaveFileMetaDataRequest of(
		String url,
		Long size,
		String filename,
		String extension,
		PreSignedUrlPrefix prefix
	) {
		return new SaveFileMetaDataRequest(url, size, filename, extension, prefix);
	}
}
