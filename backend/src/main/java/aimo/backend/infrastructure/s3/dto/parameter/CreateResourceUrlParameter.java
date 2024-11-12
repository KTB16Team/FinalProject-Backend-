package aimo.backend.infrastructure.s3.dto.parameter;

import aimo.backend.domains.member.dto.parameter.SaveFileMetaDataParameter;
import aimo.backend.infrastructure.s3.model.PresignedUrlPrefix;

public record CreateResourceUrlParameter(
	String prefix,
	String filename,
	String extension
) {

	public static CreateResourceUrlParameter of(String prefix, String filename, String extension) {
		return new CreateResourceUrlParameter(prefix, filename, extension);
	}

	public static CreateResourceUrlParameter from(PresignedUrlPrefix prefix, SaveFileMetaDataParameter parameter) {
		return new CreateResourceUrlParameter(prefix.getValue(), parameter.filename(), parameter.extension());
	}
}
