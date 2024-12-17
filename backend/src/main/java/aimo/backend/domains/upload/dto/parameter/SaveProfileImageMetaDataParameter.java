package aimo.backend.domains.upload.dto.parameter;

import aimo.backend.domains.upload.dto.request.SaveProfileImageMetaDataRequest;
import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;

public record SaveProfileImageMetaDataParameter(
	Long memberId,
	String filename,
	String extension,
	String key,
	PreSignedUrlPrefix prefix
) {

	public static SaveProfileImageMetaDataParameter from(SaveProfileImageMetaDataRequest request, Long memberId) {
		return new SaveProfileImageMetaDataParameter(
			memberId,
			request.filename(),
			request.extension(),
			request.key(),
			request.prefix()
		);
	}
}
