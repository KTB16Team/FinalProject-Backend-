package aimo.backend.domains.view.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.view.dto.IncreasePostViewParameter;
import aimo.backend.domains.view.dto.IncreasePostViewRequest;
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.view.service.PostViewMemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ViewController {

	private final PostViewMemberService postViewMemberService;

	@PostMapping("/{postId}/views")
	public ResponseEntity<DataResponse<Void>> increasePostView(
		@PathVariable("postId") IncreasePostViewRequest increasePostViewRequest) {
		Long memberId = MemberLoader.getMemberId();
		IncreasePostViewParameter increasePostViewParameter = IncreasePostViewParameter.of(memberId, increasePostViewRequest.postId());
		postViewMemberService.increasePostViewBy(increasePostViewParameter);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}
}
