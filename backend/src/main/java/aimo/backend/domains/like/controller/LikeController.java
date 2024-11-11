package aimo.backend.domains.like.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.mapper.ChildCommentLikeMapper;
import aimo.backend.common.mapper.ParentCommentLikeMapper;
import aimo.backend.common.mapper.PostLikeMapper;
import aimo.backend.domains.like.dto.parameter.LikeChildCommentParameter;
import aimo.backend.domains.like.dto.parameter.LikeParentCommentParameter;
import aimo.backend.domains.like.dto.parameter.LikePostParameter;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.service.ChildCommentLikeService;
import aimo.backend.domains.like.service.ParentCommentLikeService;
import aimo.backend.domains.like.service.PostLikeService;
import aimo.backend.common.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LikeController {

	private final PostLikeService postLikeService;
	private final MemberLoader memberLoader;
	private final ChildCommentLikeService childCommentLikeService;
	private final ParentCommentLikeService parentCommentLikeService;

	@PostMapping("/posts/{postId}/likes")
	public ResponseEntity<DataResponse<Void>> likePost(@PathVariable Long postId,
		@RequestParam("likeType") LikeType likeType) {
		LikePostParameter parameter = PostLikeMapper.toLikePostParameter(postId, likeType);
		postLikeService.likePost(parameter);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/comments/child/{childCommentId}/likes")
	public ResponseEntity<DataResponse<Void>> likeChildComment(
		@PathVariable Long childCommentId,
		@RequestParam("likeType") LikeType likeType) {
		LikeChildCommentParameter parameter = ChildCommentLikeMapper
			.toLikeChildCommentParameter(childCommentId, likeType);
		childCommentLikeService.likeChildComment(parameter);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/comments/{parentCommentId}/likes")
	public ResponseEntity<DataResponse<Void>> likeParentComment(
		@PathVariable Long parentCommentId,
		@RequestParam("likeType") LikeType likeType) {
		LikeParentCommentParameter parameter = ParentCommentLikeMapper
			.toLikeParentCommentParameter(parentCommentId, likeType);
		parentCommentLikeService.likeParentComment(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
