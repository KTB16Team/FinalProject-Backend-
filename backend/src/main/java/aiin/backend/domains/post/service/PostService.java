package aiin.backend.domains.post.service;

import static aiin.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aiin.backend.common.exception.ApiException;
import aiin.backend.domains.post.entity.Post;
import aiin.backend.domains.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private final PostRepository postRepository;

	public Post findById(Long id) {
		return postRepository.findById(id)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));
	}
}
