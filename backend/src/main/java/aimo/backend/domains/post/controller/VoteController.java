package aimo.backend.domains.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.post.model.Side;
import aimo.backend.domains.post.service.VoteService;
import aimo.backend.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class VoteController {

	private final VoteService voteService;
	private final MemberLoader memberLoader;

	@PostMapping("/{postId}/votes")
	public ResponseEntity<DataResponse<Void>> votePost(
		@PathVariable Long postId,
		@RequestParam("side") Side side
	) {
		voteService.votePost(memberLoader.getMember(), postId, side);

		return ResponseEntity.ok(DataResponse.noContent());
	}
}
