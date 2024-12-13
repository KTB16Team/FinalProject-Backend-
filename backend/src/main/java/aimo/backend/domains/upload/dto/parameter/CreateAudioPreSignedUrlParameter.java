package aimo.backend.domains.upload.dto.parameter;

public record CreateAudioPreSignedUrlParameter(String filename, String extension) {

	public static CreateAudioPreSignedUrlParameter of(String filename) {
		String extension = filename.substring(filename.lastIndexOf(".") + 1);

		return new CreateAudioPreSignedUrlParameter(filename, extension);
	}
}
