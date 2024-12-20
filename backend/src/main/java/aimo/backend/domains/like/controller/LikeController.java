package aimo.backend.domains.like.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.like.dto.parameter.LikeChildCommentParameter;
import aimo.backend.domains.like.dto.parameter.LikeParentCommentParameter;
import aimo.backend.domains.like.dto.parameter.LikePostParameter;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.service.ChildCommentLikeService;
import aimo.backend.domains.like.service.ParentCommentLikeService;
import aimo.backend.domains.like.service.PostLikeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LikeController {

	private final PostLikeService postLikeService;
	private final ChildCommentLikeService childCommentLikeService;
	private final ParentCommentLikeService parentCommentLikeService;

	@PostMapping("/comments/child/{childCommentId}/likes")
	public ResponseEntity<DataResponse<Void>> likeChildComment(
		@PathVariable Long childCommentId,
		@RequestParam("likeType") LikeType likeType
	) {
		Long memberId = MemberLoader.getMemberId();

		LikeChildCommentParameter parameter =
			LikeChildCommentParameter.of(memberId, childCommentId, likeType);

		childCommentLikeService.likeChildComment(parameter);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/comments/{commentId}/likes")
	public ResponseEntity<DataResponse<Void>> likeParentComment(
		@PathVariable Long commentId,
		@RequestParam("likeType") LikeType likeType
	) {
		Long memberId = MemberLoader.getMemberId();

		LikeParentCommentParameter parameter =
			LikeParentCommentParameter.of(memberId, commentId, likeType);

		parentCommentLikeService.likeParentComment(parameter);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/posts/{postId}/likes")
	public ResponseEntity<DataResponse<Void>> likePost(
		@PathVariable Long postId,
		@RequestParam("likeType") LikeType likeType
	) {
		Long memberId = MemberLoader.getMemberId();

		LikePostParameter parameter = LikePostParameter.of(memberId, postId, likeType);
		postLikeService.likePost(parameter);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}
}
