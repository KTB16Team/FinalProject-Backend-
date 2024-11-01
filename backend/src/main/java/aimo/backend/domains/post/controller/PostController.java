package aimo.backend.domains.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.post.dto.SavePostRequest;
import aimo.backend.domains.post.service.PostService;
import aimo.backend.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;
	private final MemberLoader memberLoader;

	@PostMapping("/{private_post_id}")
	public ResponseEntity<DataResponse<Void>> publish(@RequestBody SavePostRequest publishPostRequest) {
		postService.save(publishPostRequest, memberLoader.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}
}
