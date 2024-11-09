package aimo.backend.domains.view.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.mapper.ViewMapper;
import aimo.backend.domains.view.dto.IncreasePostViewParameter;
import aimo.backend.domains.view.dto.IncreasePostViewRequest;
import aimo.backend.domains.view.service.PostViewService;
import aimo.backend.common.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ViewController {

	private final PostViewService postViewService;
	private final MemberLoader memberLoader;

	@PostMapping("/{postId}/views")
	public ResponseEntity<DataResponse<Void>> increasePostView(
		@PathVariable("postId") IncreasePostViewRequest increasePostViewRequest) {
		Long memberId = memberLoader.getMemberId();
		IncreasePostViewParameter increasePostViewParameter = ViewMapper.toIncreasePostViewParameter(memberId, increasePostViewRequest.postId());
		postViewService.increasePostViewBy(increasePostViewParameter);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}
}
