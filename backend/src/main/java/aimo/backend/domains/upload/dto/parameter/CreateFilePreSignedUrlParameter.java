package aimo.backend.domains.upload.dto.parameter;

import aimo.backend.infrastructure.s3.dto.request.CreatePreSignedUrlRequest;
import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;

public record CreateFilePreSignedUrlParameter(
	String filename,
	String extension,
	PreSignedUrlPrefix prefix
) {
	public static CreateFilePreSignedUrlParameter of(String filename, PreSignedUrlPrefix prefix) {
		String extension = filename.substring(filename.lastIndexOf(".") + 1);

		return new CreateFilePreSignedUrlParameter(filename, extension, prefix);
	}
}
