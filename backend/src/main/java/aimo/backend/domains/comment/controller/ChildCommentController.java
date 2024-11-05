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
import aimo.backend.domains.comment.dto.request.SaveChildCommentRequest;
import aimo.backend.domains.like.service.ChildCommentLikeService;
import aimo.backend.domains.comment.service.ChildCommentService;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.util.memberLoader.MemberLoader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ChildCommentController {

	private final ChildCommentService childCommentService;
	private final MemberLoader memberLoader;
	private final ChildCommentLikeService childCommentLikeService;

	@PostMapping("/{postId}/comments/{parentCommentId}/child")
	public ResponseEntity<DataResponse<Void>> saveChildComment(
		@Valid @PathVariable("postId") Long postId,
		@Valid @PathVariable("parentCommentId") Long parentCommentId,
		@Valid @RequestBody SaveChildCommentRequest request
	) {
		Member member = memberLoader.getMember();
		childCommentService.saveChildComment(member, postId, parentCommentId, request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PutMapping("comments/child/{childCommentId}")
	public ResponseEntity<DataResponse<Void>> updateParentComment(
		@Valid @PathVariable Long childCommentId,
		@Valid @RequestBody SaveChildCommentRequest request
	) {
		Member member = memberLoader.getMember();
		childCommentService.validateAndUpdateChildComment(member, childCommentId, request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("comments/child/{childCommentId}")
	public ResponseEntity<DataResponse<Void>> deleteParentComment(
		@Valid @PathVariable Long childCommentId
	) {
		Member member = memberLoader.getMember();
		childCommentService.validateAndDeleteChildComment(member, childCommentId);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
