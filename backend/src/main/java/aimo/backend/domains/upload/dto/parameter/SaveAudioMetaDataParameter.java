package aimo.backend.domains.upload.dto.parameter;

import aimo.backend.domains.upload.dto.request.SaveAudioMetaDataRequest;

public record SaveAudioMetaDataParameter(
	String url,
	Long size,
	String filename,
	String extension
) {

	public static SaveAudioMetaDataParameter from(SaveAudioMetaDataRequest request) {
		return new SaveAudioMetaDataParameter(
			request.url(),
			request.size(),
			request.filename(),
			request.extension()
		);
	}
}
