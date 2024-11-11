package aimo.backend.common.mapper;

import aimo.backend.domains.view.dto.IncreasePostViewParameter;

public class ViewMapper {
	public static IncreasePostViewParameter toIncreasePostViewParameter(Long memberId, Long postId) {
		return new IncreasePostViewParameter(memberId, postId);
	}
}
