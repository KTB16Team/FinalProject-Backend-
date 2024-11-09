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
import aimo.backend.domains.comment.dto.request.SaveChildCommentParameter;
import aimo.backend.domains.comment.dto.request.SaveChildCommentRequest;
import aimo.backend.domains.comment.dto.request.ValidAndDeleteParentCommentParameter;
import aimo.backend.domains.comment.dto.request.ValidAndUpdateChildCommentParameter;
import aimo.backend.domains.comment.mapper.ChildCommentMapper;
import aimo.backend.domains.comment.service.ChildCommentService;
import aimo.backend.common.util.memberLoader.MemberLoader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ChildCommentController {

	private final ChildCommentService childCommentService;
	private final MemberLoader memberLoader;

	@PostMapping("/{postId}/comments/{parentCommentId}/child")
	public ResponseEntity<DataResponse<Void>> saveChildComment(
		@Valid @PathVariable("postId") Long postId,
		@Valid @PathVariable("parentCommentId") Long parentCommentId,
		@Valid @RequestBody SaveChildCommentRequest request) {
		Long memberId = memberLoader.getMemberId();
		SaveChildCommentParameter saveChildCommentParameter =
			ChildCommentMapper.toSaveChildCommentParameter(memberId, postId, parentCommentId, request.content());
		childCommentService.saveChildComment(saveChildCommentParameter);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PutMapping("comments/child/{childCommentId}")
	public ResponseEntity<DataResponse<Void>> updateChildComment(
		@Valid @PathVariable Long childCommentId,
		@Valid @RequestBody SaveChildCommentRequest request) {
		Long memberId = memberLoader.getMemberId();
		ValidAndUpdateChildCommentParameter validAndUpdateChildCommentParameter =
			ChildCommentMapper.toValidAndUpdateChildCommentParameter(memberId, childCommentId, request.content());
		childCommentService.validateAndUpdateChildComment(validAndUpdateChildCommentParameter);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("comments/child/{childCommentId}")
	public ResponseEntity<DataResponse<Void>> deleteParentComment(@Valid @PathVariable Long childCommentId) {
		Long memberId = memberLoader.getMemberId();
		ValidAndDeleteParentCommentParameter validateAndDeleteChildCommentParameter =
			ChildCommentMapper.toValidAndDeleteParentCommentParameter(memberId, childCommentId);
		childCommentService.validateAndDeleteChildComment(validateAndDeleteChildCommentParameter);
		return ResponseEntity.ok(DataResponse.ok());
	}
}
