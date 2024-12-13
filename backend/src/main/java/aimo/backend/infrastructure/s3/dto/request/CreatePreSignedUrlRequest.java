package aimo.backend.infrastructure.s3.dto.request;

import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;

public record CreatePreSignedUrlRequest(
	String filename,
	PreSignedUrlPrefix prefix
) {

	public static CreatePreSignedUrlRequest of(String filename, PreSignedUrlPrefix prefix) {
		return new CreatePreSignedUrlRequest(filename, prefix);
	}
}
