package aimo.backend.domains.member.dto;

public record SaveProfileImageMetaData(
	String filename,
	String extension,
	Long size,
	String url
) {
}
