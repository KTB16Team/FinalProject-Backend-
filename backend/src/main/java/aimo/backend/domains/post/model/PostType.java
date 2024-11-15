// PostType Enum에 각 타입에 따른 메서드 구현
package aimo.backend.domains.post.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import aimo.backend.domains.post.dto.parameter.FindPostByPostTypeParameter;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.service.PostService;

public enum PostType {

	MY("내가 쓴 글") {
		@Override
		public Page<FindPostsByPostTypeResponse> findPosts(PostService service, FindPostByPostTypeParameter parameter, Pageable pageable) {
			return service.findMyPosts(parameter.memberId(), pageable);
		}
	},
	COMMENTED("댓글 단 글") {
		@Override
		public Page<FindPostsByPostTypeResponse> findPosts(PostService service, FindPostByPostTypeParameter parameter, Pageable pageable) {
			return service.findCommentedPosts(parameter.memberId(), pageable);
		}
	},
	POPULAR("인기 글") {
		@Override
		public Page<FindPostsByPostTypeResponse> findPosts(PostService service, FindPostByPostTypeParameter parameter, Pageable pageable) {
			return service.findPopularPosts(pageable);
		}
	},
	ANY("전체 글") {
		@Override
		public Page<FindPostsByPostTypeResponse> findPosts(PostService service, FindPostByPostTypeParameter parameter, Pageable pageable) {
			return service.findAnyPosts(pageable);
		}
	};

	private final String value;

	PostType(String value) {
		this.value = value;
	}

	// 각 PostType에 따라 다른 로직을 수행할 추상 메서드 정의
	public abstract Page<FindPostsByPostTypeResponse> findPosts(PostService service, FindPostByPostTypeParameter parameter, Pageable pageable);
}
