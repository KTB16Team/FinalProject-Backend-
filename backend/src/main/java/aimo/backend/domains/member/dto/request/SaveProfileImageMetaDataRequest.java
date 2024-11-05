package aimo.backend.domains.member.dto.request;

public record SaveProfileImageMetaDataRequest(
	String filename,
	String extension,
	Long size,
	String url
) {
}
