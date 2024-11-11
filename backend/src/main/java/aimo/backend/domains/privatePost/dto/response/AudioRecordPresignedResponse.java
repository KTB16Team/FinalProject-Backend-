package aimo.backend.domains.privatePost.dto.response;


public record AudioRecordPresignedResponse(
	String presignedUrl,
	String filename
) {

	public static AudioRecordPresignedResponse of(
		String presignedUrl,
		String filename
	) {
		return new AudioRecordPresignedResponse(presignedUrl, filename);
	}
}
