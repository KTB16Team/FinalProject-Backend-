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
import aimo.backend.domains.comment.dto.request.SaveParentCommentRequest;
import aimo.backend.domains.comment.dto.request.UpdateParentCommentRequest;
import aimo.backend.domains.comment.service.ParentCommentService;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.common.util.memberLoader.MemberLoader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ParentCommentController {

	private final ParentCommentService parentCommentService;
	private final MemberLoader memberLoader;

	@PostMapping("/{postId}/comments")
	public ResponseEntity<DataResponse<Void>> saveParentComment(
		@Valid @PathVariable Long postId,
		@Valid @RequestBody SaveParentCommentRequest request) {
		Member member = memberLoader.getMember();
		parentCommentService.saveParentComment(member, postId, request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PutMapping("comments/{commentId}")
	public ResponseEntity<DataResponse<Void>> updateParentComment(
		@Valid @PathVariable Long commentId,
		@Valid @RequestBody UpdateParentCommentRequest request) {
		Member member = memberLoader.getMember();
		parentCommentService.validateAndUpdateParentComment(member, commentId, request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("comments/{commentId}")
	public ResponseEntity<DataResponse<Void>> deleteParentComment(@Valid @PathVariable Long commentId) {
		Member member = memberLoader.getMember();
		parentCommentService.validateAndDeleteParentComment(member, commentId);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
