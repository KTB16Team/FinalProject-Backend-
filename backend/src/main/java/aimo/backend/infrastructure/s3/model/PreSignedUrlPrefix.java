package aimo.backend.infrastructure.s3.model;

import java.util.function.Predicate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PreSignedUrlPrefix {

	IMAGE("image", extension -> extension.equals("png")
		|| extension.equals("jpg")
		|| extension.equals("jpeg")),
	AUDIO("audio", extension -> extension.equals("mp3")
		|| extension.equals("wav")
		|| extension.equals("ogg")
		|| extension.equals("acc")
		|| extension.equals("flac")
		|| extension.equals("m4a")),
	PROFILE("profile", extension -> extension.equals("png")
		|| extension.equals("jpg")
		|| extension.equals("jpeg")),
	TEXT("text", extension -> extension.equals("txt"));

	private final String value;
	private final Predicate<String> validator;

	public boolean isValidExtension(String extension) {
		return validator.test(extension);
	}
}