package aiin.backend.domains.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aiin.backend.common.dto.DataResponse;
import aiin.backend.domains.comment.dto.request.SaveChildCommentRequest;
import aiin.backend.domains.comment.dto.request.UpdateChildCommentRequest;
import aiin.backend.domains.comment.service.ChildCommentService;
import aiin.backend.domains.member.entity.Member;
import aiin.backend.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ChildCommentController {

	private final ChildCommentService childCommentService;
	private final MemberLoader memberLoader;

	@PostMapping("/{postId}/comments/{parentCommentId}/child")
	public ResponseEntity<DataResponse<Void>> saveChildComment(
		@PathVariable("postId") Long postId,
		@PathVariable("parentCommentId") Long parentCommentId,
		@RequestBody SaveChildCommentRequest request
	) {
		Member member = memberLoader.getMember();
		childCommentService.saveChildComment(member, postId, parentCommentId, request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PutMapping("comments/child/{childCommentId}")
	public ResponseEntity<DataResponse<Void>> updateParentComment(
		@PathVariable Long childCommentId,
		@RequestBody UpdateChildCommentRequest request
	) {
		Member member = memberLoader.getMember();
		childCommentService.validateAndUpdateChildComment(member, childCommentId, request);

		return ResponseEntity.ok(DataResponse.ok());
	}
	//
	// @DeleteMapping("comments/{commentId}")
	// public ResponseEntity<DataResponse<Void>> deleteParentComment(
	// 	@PathVariable Long commentId
	// ) {
	// 	Member member = memberLoader.getMember();
	// 	childCommentService.validateAndDeleteCHildComment(member, commentId);
	//
	// 	return ResponseEntity.ok(DataResponse.ok());
	// }
}
