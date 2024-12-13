package aimo.backend.infrastructure.s3.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PresignedUrlPrefix {

	IMAGE("image"),
	AUDIO("audio"),
	PROFILE("profile"),
	;
	private final String value;
}
