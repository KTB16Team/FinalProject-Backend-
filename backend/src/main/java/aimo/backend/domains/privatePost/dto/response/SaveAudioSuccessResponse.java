package aimo.backend.domains.privatePost.dto.response;

import aimo.backend.domains.privatePost.entity.AudioRecord;

public record SaveAudioSuccessResponse(
	String url,
	Long size,
	String filename
) {

	public static SaveAudioSuccessResponse of(String url, Long size, String filename) {
		return new SaveAudioSuccessResponse(url, size, filename);
	}

	public static SaveAudioSuccessResponse from(AudioRecord audioRecord) {
		return new SaveAudioSuccessResponse(
			audioRecord.getUrl(),
			audioRecord.getSize(),
			audioRecord.getFilename()
		);
	}
}
