package aimo.backend.domains.comment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.comment.dto.parameter.DeleteParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.SaveParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.UpdateParentCommentParameter;
import aimo.backend.domains.comment.dto.request.SaveParentCommentRequest;
import aimo.backend.domains.comment.dto.request.UpdateParentCommentRequest;
import aimo.backend.domains.comment.dto.response.FindCommentsResponse;
import aimo.backend.domains.comment.service.ParentCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ParentCommentController {

	private final ParentCommentService parentCommentService;

	@PostMapping("/{postId}/comments")
	public ResponseEntity<DataResponse<Void>> saveParentComment(
		@Valid @PathVariable Long postId,
		@Valid @RequestBody SaveParentCommentRequest request
	) {
		Long memberId = MemberLoader.getMemberId();

		SaveParentCommentParameter parameter = SaveParentCommentParameter.of(memberId, postId, request.content());
		parentCommentService.saveParentComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PutMapping("/comments/{commentId}")
	public ResponseEntity<DataResponse<Void>> updateParentComment(
		@Valid @PathVariable Long commentId,
		@Valid @RequestBody UpdateParentCommentRequest request
	) {
		Long memberId = MemberLoader.getMemberId();

		UpdateParentCommentParameter parameter = UpdateParentCommentParameter.of(
			memberId,
			commentId,
			request.content());

		parentCommentService.validateAndUpdateParentComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<DataResponse<Void>> deleteParentComment(
		@Valid @PathVariable("commentId") Long commentId
	) {
		Long memberId = MemberLoader.getMemberId();

		DeleteParentCommentParameter parameter = DeleteParentCommentParameter.of(memberId, commentId);

		parentCommentService.validateAndDeleteParentComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/{postId}/comments")
	public ResponseEntity<DataResponse<List<FindCommentsResponse>>> findComments(
		@PathVariable("postId") Long postId
	) {
		Long memberId = MemberLoader.getMemberId();

		return ResponseEntity.ok(DataResponse.from(parentCommentService.findComments(memberId, postId)));
	}
}
