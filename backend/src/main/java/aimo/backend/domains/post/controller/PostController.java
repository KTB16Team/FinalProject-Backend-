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
import aimo.backend.common.mapper.PostMapper;
import aimo.backend.domains.post.dto.parameter.DeletePostParameter;
import aimo.backend.domains.post.dto.parameter.FindPostAndCommentsByIdParameter;
import aimo.backend.domains.post.dto.parameter.FindPostByPostTypeParameter;
import aimo.backend.domains.post.dto.parameter.SavePostParameter;
import aimo.backend.domains.post.dto.requset.DeletePostRequest;
import aimo.backend.domains.post.dto.requset.FindJudgementRequest;
import aimo.backend.domains.post.dto.requset.FindPostAndCommentsRequest;
import aimo.backend.domains.post.dto.requset.SavePostRequest;
import aimo.backend.domains.post.dto.response.FindJudgementResponse;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.dto.response.SavePostResponse;
import aimo.backend.domains.post.model.PostType;
import aimo.backend.domains.post.service.PostService;
import aimo.backend.common.util.memberLoader.MemberLoader;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;
	private final MemberLoader memberLoader;

	@PostMapping
	public ResponseEntity<DataResponse<SavePostResponse>> savePost(@RequestBody @Valid SavePostRequest request) {
		SavePostParameter parameter = PostMapper.toSavePostParameter(request);
		Long postId = postService.save(parameter);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(PostMapper.toSavePostResponse(postId)));
	}

	@GetMapping
	public ResponseEntity<DataResponse<Page<FindPostsByPostTypeResponse>>> findPostsByPostType(
		@RequestParam("type") @NotNull PostType postType,
		@RequestParam("page") @NotNull Integer page,
		@RequestParam("size") @NotNull Integer size) {
		FindPostByPostTypeParameter parameter = PostMapper.toFindPostByPostTypeParameter(postType, page, size);
		return ResponseEntity.ok(DataResponse.from(postService.findPostDtosByPostType(parameter)));
	}

	@GetMapping("/{postId}")
	public ResponseEntity<DataResponse<FindPostAndCommentsByIdResponse>> findPostAndComments(
		@PathVariable("postId") FindPostAndCommentsRequest request) {
		FindPostAndCommentsByIdParameter parameter = PostMapper.toFindPostAndCommentsByIdParameter(request.postId());
		return ResponseEntity.ok(DataResponse.from(postService.findPostAndCommentsDtoById(parameter)));
	}

	@GetMapping("/{postId}/judgement")
	public ResponseEntity<DataResponse<FindJudgementResponse>> findJudgement(
		@PathVariable("postId") FindJudgementRequest request) {
		return ResponseEntity.ok(DataResponse.from(postService.findJudgementBy(request.postId())));
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<DataResponse<Void>> deletePost(@PathVariable("postId") DeletePostRequest request) {
		DeletePostParameter parameter = PostMapper.toDeletePostParameter(request.postId());
		postService.deletePostBy(parameter);
		return ResponseEntity.ok(DataResponse.noContent());
	}
}
