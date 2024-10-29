package aiin.backend.domains.comment.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SaveChildCommentRequest {
	private final String content;
}
