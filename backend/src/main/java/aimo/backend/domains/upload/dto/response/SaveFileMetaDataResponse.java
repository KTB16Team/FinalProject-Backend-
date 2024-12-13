package aimo.backend.domains.upload.dto.response;

import aimo.backend.domains.upload.entity.FileRecord;

public record SaveFileMetaDataResponse(
	String url,
	Long size,
	String filename
) {

	public static SaveFileMetaDataResponse of(String url, Long size, String filename) {
		return new SaveFileMetaDataResponse(url, size, filename);
	}

	public static SaveFileMetaDataResponse from(FileRecord fileRecord) {
		return new SaveFileMetaDataResponse(
			fileRecord.getUrl(),
			fileRecord.getSize(),
			fileRecord.getFilename()
		);
	}
}
