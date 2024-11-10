package aimo.backend.domains.member.dto.parameter;

public record SaveFileMetaDataParameter(
	Long memberId,
	String filename,
	String extension,
	Long size
) {
}
