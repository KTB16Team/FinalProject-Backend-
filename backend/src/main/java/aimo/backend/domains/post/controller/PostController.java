package aimo.backend.domains.post.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.like.service.PostLikeService;
import aimo.backend.domains.post.dto.requset.SavePostRequest;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.model.PostType;
import aimo.backend.domains.post.service.PostService;
import aimo.backend.domains.post.service.PostViewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;
	private final PostViewService postViewService;

	@PostMapping
	public ResponseEntity<DataResponse<Void>> savePost(@RequestBody @Valid SavePostRequest request) {
		postService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@GetMapping
	public ResponseEntity<DataResponse<Page<FindPostsByPostTypeResponse>>> findPostsByPostType(
		@RequestParam("type") @NotNull PostType postType, @RequestParam("page") @NotNull Integer page,
		@RequestParam("size") @NotNull Integer size) {
		return ResponseEntity.ok(DataResponse.from(postService.findPostDtosByPostType(postType, page, size)));
	}

	@GetMapping("/{postId}")
	public ResponseEntity<DataResponse<FindPostAndCommentsByIdResponse>> findPostAndComments(
		@PathVariable Long postId) {
		return ResponseEntity.ok(DataResponse.from(postService.findPostAndCommentsDtoById(postId)));
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<DataResponse<Void>> deletePost(@PathVariable Long postId) {
		postService.deletePostBy(postId);
		return ResponseEntity.ok(DataResponse.noContent());
	}

	@PostMapping("/{postId}/views")
	public void increaseView(@PathVariable Long postId) {
		postViewService.increaseViewBy(postId);
	}
}
