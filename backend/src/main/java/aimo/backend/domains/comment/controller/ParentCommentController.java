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
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.comment.dto.parameter.DeleteParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.SaveParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.UpdateParentCommentParameter;
import aimo.backend.domains.comment.dto.request.DeleteParentCommentRequest;
import aimo.backend.domains.comment.dto.request.SaveParentCommentRequest;
import aimo.backend.domains.comment.dto.request.UpdateParentCommentRequest;
import aimo.backend.domains.comment.mapper.ParentCommentMapper;
import aimo.backend.domains.comment.service.ParentCommentMemberService;
import aimo.backend.domains.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ParentCommentController {

	private final ParentCommentMemberService parentCommentMemberService;

	@PostMapping("/{postId}/comments")
	public ResponseEntity<DataResponse<Void>> saveParentComment(
		@Valid @PathVariable Long postId,
		@Valid @RequestBody SaveParentCommentRequest request) {
		Long memberId = MemberLoader.getMemberId();
		SaveParentCommentParameter parameter =
			SaveParentCommentParameter.of(memberId, postId, request.content());
		parentCommentMemberService.saveParentComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PutMapping("comments/{commentId}")
	public ResponseEntity<DataResponse<Void>> updateParentComment(
		@Valid @PathVariable Long commentId,
		@Valid @RequestBody UpdateParentCommentRequest request) {
		Long memberId = MemberLoader.getMemberId();
		UpdateParentCommentParameter parameter =
			UpdateParentCommentParameter.of(memberId, commentId, request.content());
		parentCommentMemberService.validateAndUpdateParentComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("comments/{commentId}")
	public ResponseEntity<DataResponse<Void>> deleteParentComment(
		@Valid @PathVariable("commentId") Long commentId) {
		Long memberId = MemberLoader.getMemberId();
		DeleteParentCommentParameter parameter = DeleteParentCommentParameter.of(memberId, commentId);
		parentCommentMemberService.validateAndDeleteParentComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
