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
import aimo.backend.common.mapper.PostMapper;
import aimo.backend.common.mapper.PrivatePostMapper;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.service.PostCommentService;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.service.MemberService;
import aimo.backend.domains.post.dto.SoftDeletePostParameter;
import aimo.backend.domains.post.dto.parameter.DeletePostParameter;
import aimo.backend.domains.post.dto.parameter.FindPostAndCommentsByIdParameter;
import aimo.backend.domains.post.dto.parameter.FindPostByPostTypeParameter;
import aimo.backend.domains.post.dto.parameter.SavePostParameter;
import aimo.backend.domains.post.dto.requset.FindCommentedPostsByIdRequest;
import aimo.backend.domains.post.dto.response.FindJudgementResponse;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.model.PostType;
import aimo.backend.domains.post.repository.PostRepository;
import aimo.backend.domains.privatePost.dto.parameter.DeletePrivatePostParameter;
import aimo.backend.domains.privatePost.dto.request.DeletePrivatePostRequest;
import aimo.backend.domains.privatePost.service.PrivatePostService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostService {

	private final PrivatePostService privatePostService;
	private final PostRepository postRepository;
	private final PostCommentService postCommentService;
	private final EntityManager em;

	// 글 저장
	@Transactional
	public Long save(SavePostParameter savePostParameter) {
		Member memberReference = em.getReference(Member.class, savePostParameter.memberId());
		privatePostService.publishPrivatePost(savePostParameter.privatePostId());
		Post post = PostMapper.toEntity(savePostParameter, memberReference);
		return postRepository.save(post).getId();
	}

	// 글 조회
	public Post findById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));
	}

	public FindJudgementResponse findJudgementBy(Long postId) {
		Post post = findById(postId);
		return PostMapper.toJudgement(post);
	}

	// 글 조회, dto로 응답
	public FindPostAndCommentsByIdResponse findPostAndCommentsDtoById(FindPostAndCommentsByIdParameter parameter) {
		Post post = findById(parameter.postId());
		Member memberReference = em.getReference(Member.class, parameter.memberId());
		List<ParentComment> parentComments = postCommentService.findParentCommentsByPostId(post.getId());
		return PostMapper.toFindPostAndCommentsByIdResponse(memberReference, post, parentComments);
	}

	// PostType으로 글 조회
	public Page<FindPostsByPostTypeResponse> findPostDtosByPostType(FindPostByPostTypeParameter parameter) {
		Page<FindPostsByPostTypeResponse> posts = null;
		PostType postType = parameter.postType();
		Pageable pageable = PageRequest.of(
			parameter.page(),
			parameter.size(),
			Sort.by(Sort.Direction.DESC, "id"));

		if (postType == PostType.MY) 		posts = findMyPosts(parameter.memberId(), pageable);
		if (postType == PostType.ANY) 		posts = findAnyPosts(pageable);
		if (postType == PostType.POPULAR) 	posts = findPopularPosts(pageable);
		if (postType == PostType.COMMENTED) posts = findCommentedPosts(parameter.memberId(), pageable);

		if(posts == null) throw ApiException.from(POST_TYPE_NOT_FOUND);

		return posts;
	}

	// 내가 쓴 글 조회
	private Page<FindPostsByPostTypeResponse> findMyPosts(Long memberId, Pageable pageable) {
		return postRepository
			.findAllByMember_Id(memberId, pageable)
			.map(PostMapper::toFindPostsByPostTypeResponse);
	}

	// 인기 글 조회
	private Page<FindPostsByPostTypeResponse> findPopularPosts(Pageable pageable) {
		return postRepository
			.findByViewsCount(pageable)
			.map(PostMapper::toFindPostsByPostTypeResponse);
	}

	// 최신 글 조회
	private Page<FindPostsByPostTypeResponse> findAnyPosts(Pageable pageable) {
		return postRepository
			.findAllByOrderByIdDesc(pageable)
			.map(PostMapper::toFindPostsByPostTypeResponse);
	}

	// 댓글 단 글 조회
	private Page<FindPostsByPostTypeResponse> findCommentedPosts(Long memberId, Pageable pageable) {
		List<ParentComment> parentComments = postCommentService.findParentCommentsByMemberId(memberId);

		List<FindCommentedPostsByIdRequest> commentedPosts = new ArrayList<>();

		parentComments
			.forEach((p) -> {
				FindCommentedPostsByIdRequest commentedPost = PostMapper.toFindCommentedPostsByIdRequest(p);
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
			.map(PostMapper::toFindPostsByPostTypeResponse)
			.toList();

		// Page 객체 생성
		return new PageImpl<>(pagedPosts, pageable, commentedPosts.size());
	}

	// 글 삭제
	@Transactional
	public void deletePostBy(DeletePostParameter parameter) {
		Long postId   = parameter.postId();
		Long memberId = parameter.memberId();
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

	public void softDeleteBy(SoftDeletePostParameter parameter) {
		Post post = findById(parameter.postId());
		DeletePrivatePostParameter deletePrivatePostParameter
			= PrivatePostMapper.toDeletePrivatePostParameter(post.getPrivatePostId());
		privatePostService.deletePrivatePostBy(deletePrivatePostParameter);
		post.softDelete();
	}
}
