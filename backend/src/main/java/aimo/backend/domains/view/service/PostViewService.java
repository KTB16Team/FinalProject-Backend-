package aimo.backend.domains.view.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.service.MemberService;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.service.PostService;
import aimo.backend.domains.view.dto.IncreasePostViewParameter;
import aimo.backend.domains.view.entity.PostView;
import aimo.backend.domains.view.repository.PostViewRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostViewService {

	private final PostViewRepository postViewRepository;
	private final PostService postService;
	private final EntityManager em;

	// 조회수 증가
	@Transactional(rollbackFor = Exception.class)
	public void increasePostViewBy(IncreasePostViewParameter increasePostViewParameter) {
		Long memberId = increasePostViewParameter.memberId();
		Long postId = increasePostViewParameter.postId();
		Post post = postService.findById(postId);
		Member member = em.getReference(Member.class, memberId);

		postViewRepository.findByMemberIdAndPostId(memberId, postId)
			.ifPresentOrElse(
				(postView) -> {},
				() -> postViewRepository.save(new PostView(post, member))
			);
	}
}
