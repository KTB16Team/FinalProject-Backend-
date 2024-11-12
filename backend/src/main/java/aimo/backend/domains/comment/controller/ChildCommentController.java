package aimo.backend.domains.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.comment.dto.parameter.SaveChildCommentParameter;
import aimo.backend.domains.comment.dto.request.SaveChildCommentRequest;
import aimo.backend.domains.comment.dto.parameter.ValidAndDeleteParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.ValidAndUpdateChildCommentParameter;
import aimo.backend.domains.comment.service.ChildCommentService;
import aimo.backend.common.util.memberLoader.MemberLoader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ChildCommentController {

	private final ChildCommentService childCommentService;

	@PostMapping("/{postId}/comments/{parentCommentId}/child")
	public ResponseEntity<DataResponse<Void>> saveChildComment(
		@PathVariable("postId") Long postId,
		@PathVariable("parentCommentId") Long parentCommentId,
		@Valid @RequestBody SaveChildCommentRequest request
	) {
		Long memberId = MemberLoader.getMemberId();

		SaveChildCommentParameter parameter =
			SaveChildCommentParameter.of(memberId, postId, parentCommentId, request.content());

		childCommentService.saveChildComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PutMapping("comments/child/{childCommentId}")
	public ResponseEntity<DataResponse<Void>> updateChildComment(
		@PathVariable Long childCommentId,
		@Valid @RequestBody SaveChildCommentRequest request
	) {
		Long memberId = MemberLoader.getMemberId();

		ValidAndUpdateChildCommentParameter parameter =
			ValidAndUpdateChildCommentParameter.of(memberId, childCommentId, request.content());

		childCommentService.validateAndUpdateChildComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("comments/child/{childCommentId}")
	public ResponseEntity<DataResponse<Void>> deleteParentComment(@PathVariable Long childCommentId) {
		Long memberId = MemberLoader.getMemberId();

		ValidAndDeleteParentCommentParameter parameter =
			ValidAndDeleteParentCommentParameter.of(memberId, childCommentId);

		childCommentService.validateAndDeleteChildComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
