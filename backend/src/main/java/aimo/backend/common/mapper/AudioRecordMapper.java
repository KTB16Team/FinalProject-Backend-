package aimo.backend.common.mapper;

import aimo.backend.domains.privatePost.dto.UploadAudioSuccessRequest;
import aimo.backend.domains.privatePost.entity.AudioRecord;

public class AudioRecordMapper {
	public static AudioRecord toEntity(UploadAudioSuccessRequest uploadAudioSuccessRequest) {
		return AudioRecord
			.builder()
			.url(uploadAudioSuccessRequest.url())
			.size(uploadAudioSuccessRequest.size())
			.filename(uploadAudioSuccessRequest.filename())
			.build();
	}
}
