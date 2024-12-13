package aimo.backend.domains.upload.dto.parameter;

import aimo.backend.infrastructure.s3.dto.request.CreatePreSignedUrlRequest;
import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;

public record CreateFilePreSignedUrlParameter(
	String filename,
	String extension,
	PreSignedUrlPrefix prefix
) {
	public static CreateFilePreSignedUrlParameter from(CreatePreSignedUrlRequest request) {
		String extension = request.filename().substring(request.filename().lastIndexOf(".") + 1);

		return new CreateFilePreSignedUrlParameter(request.filename(), extension, request.prefix());
	}
}
