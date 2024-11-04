package aimo.backend.domains.post.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.domains.post.service.PostViewService;
import aimo.backend.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostViewController {

	private final PostViewService postViewService;
	private final MemberLoader memberLoader;

	@PostMapping("/{postId}/views")
	public void increaseView(@PathVariable Long postId) {
		postViewService.increaseView(memberLoader.getMember(), postId);
	}
}
