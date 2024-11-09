package aimo.backend.domains.vote.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.vote.dto.SaveVotePostRequest;
import aimo.backend.domains.vote.model.Side;
import aimo.backend.domains.vote.service.VoteService;
import aimo.backend.util.memberLoader.MemberLoader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class VoteController {

	private final VoteService voteService;
	private final MemberLoader memberLoader;

	@PostMapping("/{postId}/votes")
	public ResponseEntity<DataResponse<Void>> saveVotePost(@PathVariable Long postId, @RequestParam("side") Side side) {
		Long memberId = memberLoader.getMember().getId();
		SaveVotePostRequest saveVotePostRequest = new SaveVotePostRequest(postId, memberId, side);

		voteService.votePost(saveVotePostRequest);

		return ResponseEntity.ok(DataResponse.noContent());
	}
}
