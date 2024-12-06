package aimo.backend.domains.post.service;

import static aimo.backend.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.repository.ChildCommentRepository;
import aimo.backend.domains.comment.repository.ParentCommentRepository;
import aimo.backend.domains.like.repository.PostLikeRepository;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.MemberPoint;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.member.service.MemberPointService;
import aimo.backend.domains.post.dto.parameter.DeletePostParameter;
import aimo.backend.domains.post.dto.parameter.FindPostAndCommentsByIdParameter;
import aimo.backend.domains.post.dto.parameter.FindPostByPostTypeParameter;
import aimo.backend.domains.post.dto.parameter.SavePostParameter;
import aimo.backend.domains.post.dto.response.FindJudgementResponse;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.model.PostType;
import aimo.backend.domains.post.repository.PostRepository;
import aimo.backend.domains.privatePost.service.PrivatePostService;
import aimo.backend.domains.vote.entity.Vote;
import aimo.backend.domains.vote.model.Side;
import aimo.backend.domains.vote.repository.VoteRepository;
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
	private final ChildCommentRepository childCommentRepository;
	private final PostLikeRepository postLikeRepository;
	private final VoteRepository voteRepository;
	private final MemberPointService memberPointService;

	// 글 저장
	@Transactional
	public Long save(SavePostParameter savePostParameter) {
		Member member = memberRepository.findById(savePostParameter.memberId())
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));

		privatePostService.publishPrivatePost(savePostParameter.privatePostId());
		Post post = Post.of(savePostParameter, member);
		post = postRepository.save(post);

		// 포인트 증가
		memberPointService.increaseMemberPoint(member.getId(), MemberPoint.INCREASE_POINT_FROM_POST.getPoint());

		return post.getId();
	}

	// 글 조회, dto로 응답
	public FindPostAndCommentsByIdResponse findPostAndCommentsDtoById(FindPostAndCommentsByIdParameter parameter) {
		Post post = postRepository.findById(parameter.postId())
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));
		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));
		List<ParentComment> parentComments = post.getParentComments();

		boolean postLikeExists = postLikeRepository.existsByPostIdAndMemberId(post.getId(), member.getId());
		String side = voteRepository.findByPostIdAndMemberId(post.getId(), member.getId())
			.map(Vote::getSide)
			.orElse(Side.NONE)
			.getValue();

		Integer commentsCount = countComments(post.getId());

		return FindPostAndCommentsByIdResponse.of(member, post, parentComments, postLikeExists, side, commentsCount);
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
			.findAllByOrderByPostViewsCountDesc(pageable)
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
		return postRepository
			.findPostsByCommentsWrittenByMember(memberId, pageable)
			.map(FindPostsByPostTypeResponse::from);
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

	// 댓글 수 조회
	private Integer countComments(Long postId) {
		Integer parentCommentsCount = parentCommentRepository.countByPost_Id(postId);
		Integer childCommentsCount = childCommentRepository.countByPost_Id(postId);

		return parentCommentsCount + childCommentsCount;
	}
}
