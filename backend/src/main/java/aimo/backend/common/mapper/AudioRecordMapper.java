package aimo.backend.common.mapper;

import aimo.backend.domains.privatePost.dto.SaveAudioSuccessRequest;
import aimo.backend.domains.privatePost.dto.SaveAudioSuccessResponse;
import aimo.backend.domains.privatePost.entity.AudioRecord;

public class AudioRecordMapper {

	public static AudioRecord toEntity(SaveAudioSuccessRequest saveAudioSuccessRequest) {
		return AudioRecord
			.builder()
			.url(saveAudioSuccessRequest.url())
			.size(saveAudioSuccessRequest.size())
			.filename(saveAudioSuccessRequest.filename())
			.build();
	}

	public static SaveAudioSuccessResponse toSaveAudioSuccessResponse(AudioRecord audioRecord) {
		return new SaveAudioSuccessResponse(audioRecord.getUrl(), audioRecord.getSize(), audioRecord.getFilename());
	}
}
