package aimo.backend.domains.upload.dto.parameter;

import aimo.backend.infrastructure.s3.dto.request.CreatePreSignedUrlRequest;
import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;

public record CreateFilePreSignedUrlParameter(
	String filename,
	String extension,
	PreSignedUrlPrefix prefix
) {
	public static CreateFilePreSignedUrlParameter of(String filename, String extension, PreSignedUrlPrefix prefix) {
		return new CreateFilePreSignedUrlParameter(filename, extension, prefix);
	}
}
