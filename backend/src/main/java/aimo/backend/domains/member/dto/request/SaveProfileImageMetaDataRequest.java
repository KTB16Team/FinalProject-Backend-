package aimo.backend.domains.member.dto.request;

public record SaveProfileImageMetaDataRequest(
	String filename,
	String extension,
	Long size,
	String url
) {

	public static SaveProfileImageMetaDataRequest of(String filename, String extension, Long size, String url) {
		return new SaveProfileImageMetaDataRequest(filename, extension, size, url);
	}
}
