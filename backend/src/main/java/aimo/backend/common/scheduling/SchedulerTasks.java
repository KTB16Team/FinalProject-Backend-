package aimo.backend.common.scheduling;

import static aimo.backend.common.exception.ErrorCode.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SchedulerTasks {

	@Qualifier("postLikesCount")
	private final Map<Long, AtomicInteger> postLikesCount;
	private final PostRepository postRepository;

	// 좋아요수 증가를 DB에 반영
	@Scheduled(fixedRate = 60000) // 매 1분마다 캐시를 DB에 반영
	@Transactional
	public void persistPostLikesCount() {
		postLikesCount.forEach((postId, count) -> {
			Post post = postRepository.findById(postId)
				.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));

			post.addPostLikesCount(count.get());

			count.set(0); // 캐시 초기화
		});
	}
}
