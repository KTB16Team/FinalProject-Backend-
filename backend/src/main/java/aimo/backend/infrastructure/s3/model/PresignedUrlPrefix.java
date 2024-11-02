package aimo.backend.infrastructure.s3.model;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PresignedUrlPrefix {

	IMAGE("image/"), AUDIO("audio/"),
	;
	private final String value;

	public static PresignedUrlPrefix fromValue(String value) {
		for (PresignedUrlPrefix prefix : PresignedUrlPrefix.values()) {
			if (prefix.getValue().equalsIgnoreCase(value)) {
				return prefix;
			}
		}
		throw ApiException.from(ErrorCode.INVALID_PREFIX);
	}
}
