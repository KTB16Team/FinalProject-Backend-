package aimo.backend.domains.privatePost.dto.parameter;

import org.springframework.data.domain.Pageable;

public record FindPrivatePostPreviewParameter(
	Long memberId,
	Pageable pageable
) {
}
