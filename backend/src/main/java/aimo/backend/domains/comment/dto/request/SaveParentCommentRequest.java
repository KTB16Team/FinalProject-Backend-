package aimo.backend.domains.comment.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SaveParentCommentRequest {
	private final String content;
}
