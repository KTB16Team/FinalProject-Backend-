package aimo.backend.domains.privatePost.dto.parameter;

import aimo.backend.domains.privatePost.dto.request.SaveAudioSuccessRequest;

public record SaveAudioSuccessParameter(
	String url,
	Long size,
	String filename,
	String extension
) {

	public static SaveAudioSuccessParameter from(SaveAudioSuccessRequest saveAudioSuccessRequest) {
		return new SaveAudioSuccessParameter(
			saveAudioSuccessRequest.url(),
			saveAudioSuccessRequest.size(),
			saveAudioSuccessRequest.filename(),
			saveAudioSuccessRequest.extension()
		);
	}
}
