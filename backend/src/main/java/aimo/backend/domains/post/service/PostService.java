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
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.repository.ParentCommentRepository;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.repository.MemberRepository;
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
import aimo.backend.domains.privatePost.service.PrivatePostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostService {

	private final PrivatePostService privatePostService;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final ParentCommentRepository parentCommentRepository;

	// 글 저장
	@Transactional
	public Long save(SavePostParameter savePostParameter) {
		Member member = memberRepository.findById(savePostParameter.memberId())
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));

		privatePostService.publishPrivatePost(savePostParameter.privatePostId());
		Post post = Post.of(savePostParameter, member);
		return postRepository.save(post).getId();
	}

	// 글 조회, dto로 응답
	public FindPostAndCommentsByIdResponse findPostAndCommentsDtoById(FindPostAndCommentsByIdParameter parameter) {
		Post post = postRepository.findById(parameter.postId())
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));

		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));

		List<ParentComment> parentComments = post.getParentComments();
		return FindPostAndCommentsByIdResponse.from(member, post, parentComments);
	}

	// 판결문 조회
	public FindJudgementResponse findJudgementBy(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));

		return FindJudgementResponse.from(post);
	}

	// PostType으로 글 조회
	public Page<FindPostsByPostTypeResponse> findPostDtosByPostType(FindPostByPostTypeParameter parameter) {
		PostType postType = parameter.postType();

		Pageable pageable = PageRequest.of(
			parameter.page(),
			parameter.size(),
			Sort.by(Sort.Direction.DESC, "id"));

		// 각 PostType의 execute 메서드를 호출해 로직 실행
		return postType.findPosts(this, parameter, pageable);
	}

	// 내가 쓴 글 조회
	public Page<FindPostsByPostTypeResponse> findMyPosts(Long memberId, Pageable pageable) {
		return postRepository
			.findAllByMember_Id(memberId, pageable)
			.map(FindPostsByPostTypeResponse::from);

	}

	// 인기 글 조회
	public Page<FindPostsByPostTypeResponse> findPopularPosts(Pageable pageable) {
		return postRepository
			.findByViewsCount(pageable)
			.map(FindPostsByPostTypeResponse::from);
	}

	// 최신 글 조회
	public Page<FindPostsByPostTypeResponse> findAnyPosts(Pageable pageable) {
		return postRepository
			.findAllByOrderByIdDesc(pageable)
			.map(FindPostsByPostTypeResponse::from);
	}

	// 댓글 단 글 조회
	public Page<FindPostsByPostTypeResponse> findCommentedPosts(Long memberId, Pageable pageable) {
		List<ParentComment> parentComments = parentCommentRepository.findAllByMemberId(memberId);

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

		privatePostService.unpublishPrivatePost(privatePostId);
		postRepository.deleteById(postId);
	}

	// 글 삭제 권한 확인
	private void validateDeletePost(Long memberId, Long postId) {
		if (!postRepository.existsByIdAndMember_Id(postId, memberId)) {
			throw ApiException.from(POST_DELETE_UNAUTHORIZED);
		}
	}
}
