package aimo.backend.domains.member.dto;

import aimo.backend.util.validator.ValidFileExtension;

public record CreatePresignedUrlRequest(
	@ValidFileExtension(allowedExtensions = {"jpg", "jpeg", "png", "gif", "webp"})
	String extension
) {
}
