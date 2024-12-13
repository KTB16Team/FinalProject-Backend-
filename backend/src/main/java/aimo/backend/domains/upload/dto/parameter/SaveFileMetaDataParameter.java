package aimo.backend.domains.upload.dto.parameter;

import aimo.backend.domains.upload.dto.request.SaveFileMetaDataRequest;
import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;

public record SaveFileMetaDataParameter(
	String url,
	Long size,
	String filename,
	String extension,
	PreSignedUrlPrefix prefix
) {

	public static SaveFileMetaDataParameter from(SaveFileMetaDataRequest request) {
		return new SaveFileMetaDataParameter(
			request.url(),
			request.size(),
			request.filename(),
			request.extension(),
			request.prefix()
		);
	}
}
