package aimo.backend.domains.post.service;

import static aimo.backend.common.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.service.PostCommentService;
import aimo.backend.domains.post.dto.parameter.SoftDeletePostParameter;
import aimo.backend.domains.post.dto.parameter.DeletePostParameter;
import aimo.backend.domains.post.dto.parameter.FindPostByPostTypeParameter;
import aimo.backend.domains.post.dto.requset.FindCommentedPostsByIdRequest;
import aimo.backend.domains.post.dto.response.FindJudgementResponse;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.model.PostType;
import aimo.backend.domains.post.repository.PostRepository;
import aimo.backend.domains.privatePost.dto.parameter.DeletePrivatePostParameter;
import aimo.backend.domains.privatePost.service.PrivatePostMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostService {

	private final PrivatePostMemberService privatePostMemberService;
	private final PostRepository postRepository;
	private final PostCommentService postCommentService;

	// 글 조회
	public Post findById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));
	}

	public FindJudgementResponse findJudgementBy(Long postId) {
		Post post = findById(postId);
		return FindJudgementResponse.from(post);
	}

	// PostType으로 글 조회
	public Page<FindPostsByPostTypeResponse> findPostDtosByPostType(FindPostByPostTypeParameter parameter) {
		Page<FindPostsByPostTypeResponse> posts = null;
		PostType postType = parameter.postType();

		Pageable pageable = PageRequest.of(
			parameter.page(),
			parameter.size(),
			Sort.by(Sort.Direction.DESC, "id"));

		if (postType == PostType.MY)
			posts = findMyPosts(parameter.memberId(), pageable);
		if (postType == PostType.ANY)
			posts = findAnyPosts(pageable);
		if (postType == PostType.POPULAR)
			posts = findPopularPosts(pageable);
		if (postType == PostType.COMMENTED)
			posts = findCommentedPosts(parameter.memberId(), pageable);

		if (posts == null)
			throw ApiException.from(POST_TYPE_NOT_FOUND);

		return posts;
	}

	// 내가 쓴 글 조회
	private Page<FindPostsByPostTypeResponse> findMyPosts(Long memberId, Pageable pageable) {
		return postRepository
			.findAllByMember_Id(memberId, pageable)
			.map(FindPostsByPostTypeResponse::from);

	}

	// 인기 글 조회
	private Page<FindPostsByPostTypeResponse> findPopularPosts(Pageable pageable) {
		return postRepository
			.findByViewsCount(pageable)
			.map(FindPostsByPostTypeResponse::from);
	}

	// 최신 글 조회
	private Page<FindPostsByPostTypeResponse> findAnyPosts(Pageable pageable) {
		return postRepository
			.findAllByOrderByIdDesc(pageable)
			.map(FindPostsByPostTypeResponse::from);
	}

	// 댓글 단 글 조회
	private Page<FindPostsByPostTypeResponse> findCommentedPosts(Long memberId, Pageable pageable) {
		List<ParentComment> parentComments = postCommentService.findParentCommentsByMemberId(memberId);

		List<FindCommentedPostsByIdRequest> commentedPosts = new ArrayList<>();

		parentComments
			.forEach((p) -> {
				FindCommentedPostsByIdRequest commentedPost = FindCommentedPostsByIdRequest.from(p);
				int index = commentedPosts.indexOf(commentedPost);
				if (index != -1 && commentedPosts.get(index).commentedAt().isAfter(p.getCreatedAt())) {
					commentedPosts.set(index, commentedPost);
				} else {
					commentedPosts.add(commentedPost);
				}
			});

		commentedPosts.sort((a, b) -> b.commentedAt().compareTo(a.commentedAt()));

		// Pageable에 맞게 부분 리스트 추출
		int start = (int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), commentedPosts.size());

		List<FindPostsByPostTypeResponse> pagedPosts = commentedPosts
			.subList(start, end)
			.stream()
			.map(FindPostsByPostTypeResponse::from)
			.toList();

		// Page 객체 생성
		return new PageImpl<>(pagedPosts, pageable, commentedPosts.size());
	}

	// 글 삭제
	@Transactional
	public void deletePostBy(DeletePostParameter parameter) {
		Long postId = parameter.postId();
		Long memberId = parameter.memberId();

		validateDeletePost(memberId, postId);

		Long privatePostId = postRepository
			.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND))
			.getPrivatePostId();

		privatePostMemberService.unpublishPrivatePost(privatePostId);
		postRepository.deleteById(postId);
	}

	// 글 삭제 권한 확인
	private void validateDeletePost(Long memberId, Long postId) {
		Boolean exists = postRepository.existsByIdAndMember_Id(postId, memberId);

		if (!exists) {
			throw ApiException.from(POST_DELETE_UNAUTHORIZED);
		}
	}
}
