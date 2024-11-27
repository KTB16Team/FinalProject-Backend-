package aimo.backend.domains.like.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.like.entity.PostLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.repository.PostLikeRepository;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.like.dto.parameter.LikePostParameter;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

	private final PostLikeRepository postLikeRepository;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	@Transactional(rollbackFor = Exception.class)
	public void likePost(LikePostParameter parameter) {
		Long postId = parameter.postId();
		Long memberId = parameter.memberId();
		LikeType likeType = parameter.likeType();

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));

		// 존재 확인
		validatePostExist(postId);
		validateMemberExist(memberId);

		if (likeType == LikeType.LIKE) {
			// 라이크가 존재하면 중복 등록 방지
			if (postLikeRepository.existsByPostIdAndMemberId(postId, memberId))
				return;

			// 라이크 등록
			PostLike postLike = PostLike.builder()
				.postId(postId)
				.memberId(memberId)
				.build();
			postLikeRepository.save(postLike);

			// 포스트 라이크 수 증가
			post.increasePostLikesCount();
			return ;
		}

		// 라이크가 이미 존재하면 삭제
		postLikeRepository.deleteByMemberIdAndPostId(memberId, postId);
		post.decreasePostLikesCount();
	}

	// 글 존재 여부
	private void validatePostExist(Long postId) {
		if (!postRepository.existsById(postId)) {
			throw ApiException.from(POST_NOT_FOUND);
		}
	}

	// 멤버 존재 여부
	private void validateMemberExist(Long memberId) {
		if (!memberRepository.existsById(memberId)) {
			throw ApiException.from(MEMBER_NOT_FOUND);
		}
	}
}
