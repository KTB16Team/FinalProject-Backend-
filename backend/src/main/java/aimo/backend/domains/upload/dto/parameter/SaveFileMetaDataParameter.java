package aimo.backend.domains.upload.dto.parameter;

import aimo.backend.domains.upload.dto.request.SaveFileMetaDataRequest;
import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;

public record SaveFileMetaDataParameter(
	String filename,
	String extension,
	String key,
	PreSignedUrlPrefix prefix
) {

	public static SaveFileMetaDataParameter from(SaveFileMetaDataRequest request) {
		return new SaveFileMetaDataParameter(
			request.filename(),
			request.extension(),
			request.key(),
			request.prefix()
		);
	}
}
