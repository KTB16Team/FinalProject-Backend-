package aimo.backend.domains.view.service;

import static aimo.backend.common.exception.ErrorCode.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
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
	private final PostViewRepository postViewRepository;
	@Qualifier("postViewsCount")
	private final Map<Long, AtomicInteger> postViewsCount;

	@Transactional(rollbackFor = Exception.class)
	public void increasePostViewBy(IncreasePostViewParameter parameter) {
		Long memberId = parameter.memberId();
		Long postId = parameter.postId();
		String postViewId = PostView.generateId(postId, memberId);

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));

		// PostView가 존재하지 않을 경우에만 저장 및 조회수 증가
		if (!postViewRepository.existsById(postViewId)) {
			postViewRepository.save(PostView.builder()
				.postId(postId)
				.memberId(memberId)
				.build());
			postViewsCount.computeIfAbsent(postId, id -> new AtomicInteger(0))
				.incrementAndGet();
		}
	}

}
