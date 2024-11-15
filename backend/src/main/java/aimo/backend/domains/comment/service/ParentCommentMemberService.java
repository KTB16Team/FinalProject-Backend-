package aimo.backend.domains.comment.service;

import static aimo.backend.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.comment.dto.parameter.DeleteParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.SaveParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.UpdateParentCommentParameter;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.repository.ParentCommentRepository;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.member.service.MemberService;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.service.PostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentCommentMemberService {

	private final PostService postService;
	private final ParentCommentRepository parentCommentRepository;
	private final MemberRepository memberRepository;
	private final MemberService memberService;

	// 부모 댓글 권한 확인
	private void validateParentCommentAuthority(Long memberId, Long commentId) {
		Boolean exists = parentCommentRepository.existsByIdAndMember_Id(commentId, memberId);

		if (!exists) {
			throw ApiException.from(UNAUTHORIZED_PARENT_COMMENT);
		}
	}

	// 부모 댓글 저장
	@Transactional(rollbackFor = ApiException.class)
	public void saveParentComment(SaveParentCommentParameter parameter) {
		Long memberId = parameter.memberId();
		Long postId = parameter.postId();

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));
		Post post = postService.findById(postId);

		ParentComment parentComment = ParentComment.of(member, post, parameter.content());

		parentCommentRepository.save(parentComment);
	}

	// 부모 댓글 수정
	@Transactional(rollbackFor = ApiException.class)
	public void validateAndUpdateParentComment(UpdateParentCommentParameter parameter) {
		Long memberId = parameter.memberId();
		Long parentCommentId = parameter.parentCommentId();

		validateParentCommentAuthority(memberId, parentCommentId);

		ParentComment parentComment = parentCommentRepository.findById(parentCommentId)
			.orElseThrow(() -> ApiException.from(PARENT_COMMENT_NOT_FOUND));

		parentComment.updateContent(parameter.content());
	}

	// 부모 댓글 삭제
	@Transactional(rollbackFor = ApiException.class)
	public void validateAndDeleteParentComment(DeleteParentCommentParameter parameter) {
		Long memberId = parameter.memberId();
		Long commentId = parameter.parentCommentId();

		validateParentCommentAuthority(memberId, commentId);

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

	// 자식 댓글이 없고 삭제된 부모 댓글이면 삭제
	public void deleteIfParentCommentIsDeletedAndChildrenIsEmpty(ParentComment parentComment){
		// 부모 댓글이 삭제되지 않은 상태면 return
		if (!parentComment.getIsDeleted()) {
			return;
		}
		// 자식 댓글이 존재하면 return
		if(!parentComment.getChildComments().isEmpty()){
			return;
		}

		parentCommentRepository.delete(parentComment);
	}
}
