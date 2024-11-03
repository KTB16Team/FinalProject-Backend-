package aimo.backend.domains.privatePost.dto;

import aimo.backend.infrastructure.s3.model.PresignedUrlPrefix;

public record CreateResourceUrl(
	String prefix,
	String filename,
	String extension
) {
}
