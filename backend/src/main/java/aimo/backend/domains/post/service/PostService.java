package aimo.backend.domains.post.service;

import static aimo.backend.common.exception.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.mapper.PostMapper;
import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.service.ChildCommentService;
import aimo.backend.domains.comment.service.ParentCommentService;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.dto.SavePostRequest;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.model.PostType;
import aimo.backend.domains.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private final PostRepository postRepository;
	private final ChildCommentService childCommentService;
	private final ParentCommentService parentCommentService;

	// 글 저장
	@Transactional
	public void save(SavePostRequest savePostRequest, Member member) {
		postRepository.save(PostMapper.toEntity(savePostRequest, member));
	}

	// 글 조회
	public Post findById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));
	}

	// PostType으로 글 조회
	public Page<FindPostsByPostTypeResponse> findPostDtosByPostType(Member member, PostType postType, Integer page, Integer size) {
		Page<Post> posts;
		if (postType == PostType.MY) {
			posts = findMyPosts(member.getId(), PageRequest.of(page, size));
		} else if (postType == PostType.POPULAR) {
			posts = findPopularPosts(PageRequest.of(page, size));
		} else if (postType == PostType.COMMENTED) {
			posts = findCommentedPosts(member.getId(), PageRequest.of(page, size));
		} else {
			posts = findAnyPosts(PageRequest.of(page, size));
		}

		return posts
			.map(PostMapper::toFindPostsByPostTypeResponse);
	}

	// 내가 쓴 글 조회
	private Page<Post> findMyPosts(Long memberId, Pageable pageable) {
		return postRepository.findAllByMember_Id(memberId, pageable);
	}

	// 인기 글 조회
	private Page<Post> findPopularPosts(Pageable pageable) {
		return postRepository.findByViewsCount(pageable);
	}

	// 최신 글 조회
	private Page<Post> findAnyPosts(Pageable pageable) {
		return postRepository.findPosts(pageable);
	}

	// 댓글 단 글 조회
	private Page<Post> findCommentedPosts(Long memberId, Pageable pageable) {
		List<ParentComment> parentComments = parentCommentService.findByMemberId(memberId);
		List<Post> posts1 = parentComments.stream()
			.map(ParentComment::getPost)
			.collect(Collectors.toList());

		List<ChildComment> childComments = childCommentService.findByMemberId(memberId);
		List<Post> posts2 = childComments.stream()
			.map(ChildComment::getPost)
			.collect(Collectors.toList());

		// 중복 제거
		posts1.addAll(posts2);
		List<Post> posts = posts1.stream()
			.distinct()
			.toList();

		// Pageable에 맞게 부분 리스트 추출
		int start = (int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), posts.size());
		List<Post> pagedPosts = posts.subList(start, end);

		// Page 객체 생성
		return new PageImpl<>(pagedPosts, pageable, posts.size());
	}

}
