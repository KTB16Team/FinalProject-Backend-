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
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.dto.requset.SavePostRequest;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.model.PostType;
import aimo.backend.domains.post.repository.PostRepository;
import aimo.backend.domains.privatePost.service.PrivatePostService;
import aimo.backend.util.memberLoader.MemberLoader;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private final PrivatePostService privatePostService;
	private final PostRepository postRepository;
	private final PostCommentService postCommentService;
	private final MemberLoader memberLoader;

	// 글 저장
	@Transactional
	public void save(SavePostRequest savePostRequest) {
		Member member = memberLoader.getMember();
		privatePostService.publishPrivatePost(savePostRequest.privatePostId());
		postRepository.save(PostMapper.toEntity(savePostRequest, member));
	}

	// 글 조회
	public Post findById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));
	}

	// 글 조회, dto로 응답
	public FindPostAndCommentsByIdResponse findPostAndCommentsDtoById(Long postId) {
		Post post = findById(postId);
		Member member = memberLoader.getMember();
		// 부모 댓글 조회
		List<ParentComment> parentComments = postCommentService.findParentCommentsByPostId(postId);

		// dto로 변환
		return PostMapper.toFindPostAndCommentsByIdResponse(member, post, parentComments);
	}

	// PostType으로 글 조회
	public Page<FindPostsByPostTypeResponse> findPostDtosByPostType(
		PostType postType,
		Integer page,
		Integer size
	) {
		Page<Post> posts;
		Member member = memberLoader.getMember();
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
		return postRepository.findAllByOrderByIdDesc(pageable);
	}

	// 댓글 단 글 조회
	private Page<Post> findCommentedPosts(Long memberId, Pageable pageable) {
		List<ParentComment> parentComments = postCommentService.findParentCommentsByMemberId(memberId);
		List<Post> posts1 = parentComments.stream()
			.map(ParentComment::getPost)
			.collect(Collectors.toList());

		List<ChildComment> childComments = postCommentService.findChildCommentsByMemberId(memberId);
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

	// 글 삭제
	@Transactional
	public void deletePost(Long postId) {
		Long memberId = memberLoader.getMember().getId();
		validateDeletePost(memberId, postId);

		Long privatePostId = postRepository
			.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND))
			.getPrivatePostId();

		privatePostService.unpublishPrivatePost(privatePostId);
		postRepository.deleteById(postId);
	}

	// 글 삭제 권한 확인
	public void validateDeletePost(Long memberId, Long postId) {
		Boolean exists = postRepository.existsByIdAndMember_Id(postId, memberId);

		if (!exists) {
			throw ApiException.from(POST_DELETE_UNAUTHORIZED);
		}
	}
}
