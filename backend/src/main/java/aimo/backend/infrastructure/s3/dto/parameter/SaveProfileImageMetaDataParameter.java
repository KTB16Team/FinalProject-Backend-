package aimo.backend.infrastructure.s3.dto.parameter;

public record SaveProfileImageMetaDataParameter(
	Long memberId,
	String filename,
	String extension,
	Long size
) {

	public static SaveProfileImageMetaDataParameter of(
		Long memberId,
		String filename,
		String extension,
		Long size
	) {
		return new SaveProfileImageMetaDataParameter(memberId, filename, extension, size);
	}
}
