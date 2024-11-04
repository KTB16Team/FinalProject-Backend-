package aimo.backend.domains.like.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.service.ChildCommentLikeService;
import aimo.backend.domains.like.service.ParentCommentLikeService;
import aimo.backend.domains.like.service.PostLikeService;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class LikeController {

	private final PostLikeService postLikeService;
	private final MemberLoader memberLoader;
	private final ChildCommentLikeService childCommentLikeService;
	private final ParentCommentLikeService parentCommentLikeService;

	@PostMapping("/{postId}/likes")
	public ResponseEntity<DataResponse<Void>> likePost(
		@PathVariable Long postId,
		@RequestParam("likeType") LikeType likeType
	) {
		Member member = memberLoader.getMember();
		postLikeService.likePost(member, postId, likeType);

		return ResponseEntity.ok(DataResponse.noContent());
	}

	@PostMapping("/comments/child/{childCommentId}/like")
	public ResponseEntity<DataResponse<Void>> likeChildComment(
		@PathVariable Long childCommentId,
		@RequestParam("likeType") LikeType likeType
	) {
		Member member = memberLoader.getMember();
		childCommentLikeService.likeChildComment(member, childCommentId, likeType);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/comments/{parentCommentId}/like")
	public ResponseEntity<DataResponse<Void>> likeParentComment(
		@PathVariable Long parentCommentId,
		@RequestParam("likeType") LikeType likeType
	) {
		Member member = memberLoader.getMember();
		parentCommentLikeService.likeParentComment(member, parentCommentId, likeType);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
