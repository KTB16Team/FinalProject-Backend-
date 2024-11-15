package aimo.backend.domains.view.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.repository.PostRepository;
import aimo.backend.domains.view.dto.IncreasePostViewParameter;
import aimo.backend.domains.view.entity.PostView;
import aimo.backend.domains.view.repository.PostViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostViewService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final PostViewRepository postViewRepository;

	@Transactional(rollbackFor = Exception.class)
	public void increasePostViewBy(IncreasePostViewParameter parameter) {
		Long memberId = parameter.memberId();
		Long postId = parameter.postId();

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));

		postViewRepository.findByMemberIdAndPostId(memberId, postId)
			.ifPresentOrElse(
				(postView) -> {},
				() -> postViewRepository.save(new PostView(post, member))
			);
	}

}
