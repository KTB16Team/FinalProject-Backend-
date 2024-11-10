package aimo.backend.domains.comment.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.comment.dto.parameter.DeleteParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.SaveParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.UpdateParentCommentParameter;
import aimo.backend.domains.comment.dto.request.SaveParentCommentRequest;
import aimo.backend.domains.comment.dto.request.UpdateParentCommentRequest;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.mapper.ParentCommentMapper;
import aimo.backend.domains.comment.repository.ParentCommentRepository;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.service.MemberService;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.service.PostService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentCommentService {

	private final ParentCommentMapper parentCommentMapper;
	private final PostService postService;
	private final ParentCommentRepository parentCommentRepository;
	private final EntityManager em;

	// 부모 댓글 권한 확인
	private void validateParentCommentAuthority(Member member, Long commentId) {
		Boolean exists = parentCommentRepository.existsByIdAndMember(commentId, member);

		if (!exists) {
			throw ApiException.from(UNAUTHORIZED_PARENT_COMMENT);
		}
	}

	// 부모 댓글 저장
	@Transactional(rollbackFor = ApiException.class)
	public void saveParentComment(SaveParentCommentParameter parameter) {
		Long memberId = parameter.memberId();
		Long postId = parameter.postId();
		Member member = em.getReference(Member.class, memberId);
		Post post = postService.findById(postId);
		ParentComment parentComment = parentCommentMapper.toEntity(member, post, parameter.content());

		parentCommentRepository.save(parentComment);
	}

	// 부모 댓글 수정
	@Transactional(rollbackFor = ApiException.class)
	public void validateAndUpdateParentComment(UpdateParentCommentParameter parameter) {
		Member member = em.getReference(Member.class, parameter.memberId());
		validateParentCommentAuthority(member, parameter.parentCommentId());

		ParentComment parentComment = parentCommentRepository.findById(parameter.parentCommentId())
			.orElseThrow(() -> ApiException.from(PARENT_COMMENT_NOT_FOUND));

		parentComment.updateContent(parameter.content());
	}

	// 부모 댓글 삭제
	@Transactional(rollbackFor = ApiException.class)
	public void validateAndDeleteParentComment(DeleteParentCommentParameter parameter) {
		Member member = em.getReference(Member.class, parameter.memberId());
		Long commentId = parameter.parentCommentId();
		validateParentCommentAuthority(member, commentId);

		parentCommentRepository.findById(commentId)
			.ifPresent((parentComment) -> {
				if(!parentComment.getChildComments().isEmpty()){
					parentComment.deleteParentCommentSoftlyWithContent();
					return;
				}
				parentCommentRepository.delete(parentComment);
			});
	}

	// 부모 댓글 조회
	public ParentComment findById(Long commentId) {
		return parentCommentRepository.findById(commentId)
			.orElseThrow(() -> ApiException.from(PARENT_COMMENT_NOT_FOUND));
	}

	public void deleteIfChildrenIsEmpty(ParentComment parentComment){
		if(parentComment.getChildComments().isEmpty()){
			parentCommentRepository.delete(parentComment);
		}
	}
}
