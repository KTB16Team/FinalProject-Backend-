package aimo.backend.domains.upload.dto.response;

import aimo.backend.domains.privatePost.entity.AudioRecord;

public record SaveAudioMetaDataResponse(
	String url,
	Long size,
	String filename
) {

	public static SaveAudioMetaDataResponse of(String url, Long size, String filename) {
		return new SaveAudioMetaDataResponse(url, size, filename);
	}

	public static SaveAudioMetaDataResponse from(AudioRecord audioRecord) {
		return new SaveAudioMetaDataResponse(
			audioRecord.getUrl(),
			audioRecord.getSize(),
			audioRecord.getFilename()
		);
	}
}
