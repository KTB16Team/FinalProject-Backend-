package aimo.backend.domains.upload.dto.request;

import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;
import jakarta.validation.constraints.NotNull;

public record SaveFileMetaDataRequest(
	@NotNull(message = "key가 필요합니다.")
	String key,
	@NotNull(message = "파일 이름이 필요합니다.")
	String filename,
	@NotNull(message = "파일 확장자가 필요합니다.")
	String extension,
	@NotNull(message = "prefix가 필요합니다.")
	PreSignedUrlPrefix prefix
) {

	public static SaveFileMetaDataRequest of(
		String key,
		String filename,
		String extension,
		PreSignedUrlPrefix prefix
	) {
		return new SaveFileMetaDataRequest(key, filename, extension, prefix);
	}
}
