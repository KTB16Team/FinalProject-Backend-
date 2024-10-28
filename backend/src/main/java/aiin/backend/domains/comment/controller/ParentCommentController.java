package aiin.backend.domains.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aiin.backend.common.dto.DataResponse;
import aiin.backend.domains.comment.dto.request.SaveParentCommentRequest;
import aiin.backend.domains.comment.service.ParentCommentService;
import aiin.backend.domains.member.entity.Member;
import aiin.backend.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ParentCommentController {

	private final ParentCommentService parentCommentService;
	private final MemberLoader memberLoader;

	@PostMapping("/{postId}/comments")
	public ResponseEntity<DataResponse<Void>> saveParentComment(
		@PathVariable Long postId,
		@RequestBody SaveParentCommentRequest request
	) {
		Member member = memberLoader.getMember();
		parentCommentService.saveParentComment(postId, member, request);

		return ResponseEntity.ok(DataResponse.ok());
	}

}
