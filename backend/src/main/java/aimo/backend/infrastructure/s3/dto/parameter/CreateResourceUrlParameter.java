package aimo.backend.infrastructure.s3.dto.parameter;

import aimo.backend.domains.upload.dto.parameter.SaveProfileImageMetaDataParameter;
import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;

public record CreateResourceUrlParameter(
	String prefix,
	String filename,
	String extension
) {

	public static CreateResourceUrlParameter of(String prefix, String filename, String extension) {
		return new CreateResourceUrlParameter(prefix, filename, extension);
	}

	public static CreateResourceUrlParameter from(PreSignedUrlPrefix prefix, SaveProfileImageMetaDataParameter parameter) {
		return new CreateResourceUrlParameter(prefix.getValue(), parameter.filename(), parameter.extension());
	}
}
