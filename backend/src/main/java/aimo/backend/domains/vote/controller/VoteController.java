package aimo.backend.domains.vote.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.vote.dto.SaveVotePostParameter;
import aimo.backend.domains.vote.dto.SaveVotePostRequest;
import aimo.backend.domains.vote.model.Side;
import aimo.backend.domains.vote.service.VoteService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class VoteController {

	private final VoteService voteService;

	@PostMapping("/{postId}/votes")
	public ResponseEntity<DataResponse<Void>> saveVotePost(
		@PathVariable Long postId,
		@RequestParam("side") Side side
	) {
		Long memberId = MemberLoader.getMemberId();
		SaveVotePostParameter saveVotePostParameter =
			SaveVotePostParameter.of(memberId, postId, side);
		voteService.votePost(saveVotePostParameter);
		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}
}
