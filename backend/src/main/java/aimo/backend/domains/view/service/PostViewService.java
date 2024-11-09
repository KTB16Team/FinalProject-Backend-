package aimo.backend.domains.view.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.service.PostService;
import aimo.backend.domains.view.dto.IncreasePostViewRequest;
import aimo.backend.domains.view.entity.PostView;
import aimo.backend.domains.view.repository.PostViewRepository;
import aimo.backend.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostViewService {

	private final PostViewRepository postViewRepository;
	private final PostService postService;
	private final MemberLoader memberLoader;

	// 조회수 증가
	@Transactional(rollbackFor = Exception.class)
	public void increasePostViewBy(IncreasePostViewRequest increasePostViewRequest) {
		Long postId = increasePostViewRequest.postId();
		Member member = memberLoader.getMember();
		Post post = postService.findById(postId);

		postViewRepository.findByMemberIdAndPostId(member.getId(), postId)
			.ifPresentOrElse(
				(postView) -> {},
				() -> postViewRepository.save(new PostView(post, member))
			);
	}
}
