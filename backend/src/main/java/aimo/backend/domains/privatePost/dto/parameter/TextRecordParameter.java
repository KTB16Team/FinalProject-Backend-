package aimo.backend.domains.privatePost.dto.parameter;

import aimo.backend.domains.privatePost.dto.request.TextRecordRequest;
import aimo.backend.domains.privatePost.model.OriginType;

public record TextRecordParameter(
	String title,
	String script,
	OriginType originalType
) {

	public static TextRecordParameter from(TextRecordRequest request) {
		return new TextRecordParameter(request.title(), request.script(), request.originalType());
	}
}
