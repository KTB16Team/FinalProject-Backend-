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
import aimo.backend.domains.comment.dto.parameter.DeleteParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.SaveParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.UpdateParentCommentParameter;
import aimo.backend.domains.comment.dto.request.DeleteParentCommentRequest;
import aimo.backend.domains.comment.dto.request.SaveParentCommentRequest;
import aimo.backend.domains.comment.dto.request.UpdateParentCommentRequest;
import aimo.backend.domains.comment.mapper.ParentCommentMapper;
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

	@PostMapping("/{postId}/comments")
	public ResponseEntity<DataResponse<Void>> saveParentComment(
		@Valid @PathVariable Long postId,
		@Valid @RequestBody SaveParentCommentRequest request) {
		SaveParentCommentParameter parameter = ParentCommentMapper.toSaveParentCommentParameter(postId, request.content());
		parentCommentService.saveParentComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PutMapping("comments/{commentId}")
	public ResponseEntity<DataResponse<Void>> updateParentComment(
		@Valid @PathVariable Long commentId,
		@Valid @RequestBody UpdateParentCommentRequest request) {
		UpdateParentCommentParameter parameter = ParentCommentMapper.toUpdateParentCommentParameter(commentId, request.content());
		parentCommentService.validateAndUpdateParentComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("comments/{commentId}")
	public ResponseEntity<DataResponse<Void>> deleteParentComment(
		@Valid @PathVariable("commentId") DeleteParentCommentRequest deleteParentCommentRequest) {
		DeleteParentCommentParameter parameter = ParentCommentMapper.toDeleteParentCommentParameter(deleteParentCommentRequest.commentId());
		parentCommentService.validateAndDeleteParentComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
