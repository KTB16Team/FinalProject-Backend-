package aiin.backend.domains.comment.service;

import static aiin.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aiin.backend.common.exception.ApiException;
import aiin.backend.domains.comment.dto.request.SaveParentCommentRequest;
import aiin.backend.domains.comment.dto.request.UpdateParentCommentRequest;
import aiin.backend.domains.comment.entity.ParentComment;
import aiin.backend.domains.comment.mapper.ParentCommentMapper;
import aiin.backend.domains.comment.repository.ParentCommentRepository;
import aiin.backend.domains.member.entity.Member;
import aiin.backend.domains.post.entity.Post;
import aiin.backend.domains.post.service.PostService;
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

		parentComment.updateContent(request.getContent());
	}

	// 부모 댓글 삭제
	public void validateAndDeleteParentComment(Member member, Long commentId) {
		validateParentCommentAuthority(member, commentId);

		parentCommentRepository.deleteById(commentId);
	}

	// 부모 댓글 조회
	public ParentComment findById(Long commentId) {
		return parentCommentRepository.findById(commentId)
			.orElseThrow(() -> ApiException.from(PARENT_COMMENT_NOT_FOUND));
	}
}
