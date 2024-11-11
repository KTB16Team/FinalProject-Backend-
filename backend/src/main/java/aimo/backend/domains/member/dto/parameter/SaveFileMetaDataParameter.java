package aimo.backend.domains.member.dto.parameter;

public record SaveFileMetaDataParameter(
	Long memberId,
	String filename,
	String extension,
	Long size
) {

	public static SaveFileMetaDataParameter of(Long memberId, String filename, String extension, Long size) {
		return new SaveFileMetaDataParameter(memberId, filename, extension, size);
	}
}
