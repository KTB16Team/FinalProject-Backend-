package aimo.backend.domains.comment.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UpdateParentCommentRequest {
	private final String content;
}
