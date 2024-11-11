package aimo.backend.domains.member.dto.parameter;

import aimo.backend.infrastructure.s3.dto.request.SaveFileMetaDataRequest;

public record SaveFileMetaDataParameter(
	Long memberId,
	String filename,
	String extension,
	Long size
) {

	public static SaveFileMetaDataParameter of(Long memberId, String filename, String extension, Long size) {
		return new SaveFileMetaDataParameter(memberId, filename, extension, size);
	}

	public static SaveFileMetaDataParameter from(Long memberId, SaveFileMetaDataRequest request) {
		return new SaveFileMetaDataParameter(memberId, request.filename(), request.extension(), request.size());
	}
}
