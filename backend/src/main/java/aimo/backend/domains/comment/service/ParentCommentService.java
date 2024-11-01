package aimo.backend.domains.comment.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.comment.dto.request.SaveParentCommentRequest;
import aimo.backend.domains.comment.dto.request.UpdateParentCommentRequest;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.mapper.ParentCommentMapper;
import aimo.backend.domains.comment.repository.ParentCommentRepository;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.service.PostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentCommentService {

	private final ParentCommentMapper parentCommentMapper;
	private final PostService postService;
	private final ParentCommentRepository parentCommentRepository;

	// 부모 댓글 권한 확인
	private void validateParentCommentAuthority(Member member, Long commentId) {
		Boolean exists = parentCommentRepository.existsByIdAndMember(commentId, member);

		if (!exists) {
			throw ApiException.from(UNAUTHORIZED_PARENT_COMMENT);
		}
	}

	// 부모 댓글 저장
	@Transactional(rollbackFor = ApiException.class)
	public void saveParentComment(Member member, Long postId, SaveParentCommentRequest request) {
		Post post = postService.findById(postId);
		ParentComment parentComment = parentCommentMapper.from(request, member, post);

		parentCommentRepository.save(parentComment);
	}

	// 부모 댓글 수정
	@Transactional(rollbackFor = ApiException.class)
	public void validateAndUpdateParentComment(Member member, Long commentId, UpdateParentCommentRequest request) {
		validateParentCommentAuthority(member, commentId);

		ParentComment parentComment = parentCommentRepository.findById(commentId)
			.orElseThrow(() -> ApiException.from(PARENT_COMMENT_NOT_FOUND));

		parentComment.updateContent(request.content());
	}

	// 부모 댓글 삭제
	@Transactional(rollbackFor = ApiException.class)
	public void validateAndDeleteParentComment(Member member, Long commentId) {
		validateParentCommentAuthority(member, commentId);

		parentCommentRepository.findById(commentId)
			.ifPresent(ParentComment::deleteChildCommentSoftly);
	}

	// 부모 댓글 조회
	public ParentComment findById(Long commentId) {
		return parentCommentRepository.findById(commentId)
			.orElseThrow(() -> ApiException.from(PARENT_COMMENT_NOT_FOUND));
	}
}
