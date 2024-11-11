package aimo.backend.common.mapper;

import aimo.backend.domains.privatePost.dto.request.TextRecordRequest;
import aimo.backend.domains.privatePost.entity.TextRecord;

public class TextRecordMapper {
	public static TextRecord toEntity(TextRecordRequest request) {
		return TextRecord
			.builder()
			.content(request.content())
			.build();
	}
}
