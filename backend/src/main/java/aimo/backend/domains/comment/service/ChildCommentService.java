package aimo.backend.domains.comment.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.comment.dto.parameter.SaveChildCommentParameter;
import aimo.backend.domains.comment.dto.parameter.ValidAndDeleteParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.ValidAndUpdateChildCommentParameter;
import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.repository.ChildCommentRepository;
import aimo.backend.domains.comment.repository.ParentCommentRepository;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.IncreasePoint;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.member.service.MemberPointService;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildCommentService {

	private final ChildCommentRepository childCommentRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final ParentCommentService parentCommentService;
	private final ParentCommentRepository parentCommentRepository;
	private final MemberPointService memberPointService;

	//자식 댓글 권한 확인
	private void validateChildCommentAuthority(Long memberId, Long childCommentId) {
		Boolean exists = childCommentRepository.existsByIdAndMember_Id(childCommentId, memberId);

		if (!exists) {
			throw ApiException.from(UNAUTHORIZED_CHILD_COMMENT);
		}
	}

	//자식 댓글 저장
	@Transactional(rollbackFor = ApiException.class)
	public void saveChildComment(SaveChildCommentParameter parameter) {
		Long postId = parameter.postId();
		Long parentCommentId = parameter.parentCommentId();
		String content = parameter.content();

		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));
		ParentComment parentComment = parentCommentRepository.findById(parentCommentId)
			.orElseThrow(() -> ApiException.from(PARENT_COMMENT_NOT_FOUND));

		ChildComment childComment = ChildComment.of(content, member, parentComment, post);
		childCommentRepository.save(childComment);

		// 멤버 포인트 증가
		memberPointService.checkAndIncreaseMemberPoint(member.getId(), IncreasePoint.COMMENT);
	}

	//자식 댓글 수정
	@Transactional(rollbackFor = ApiException.class)
	public void validateAndUpdateChildComment(
		ValidAndUpdateChildCommentParameter parameter
	) {
		Long memberId = parameter.memberId();
		Long childCommentId = parameter.childCommentId();
		String content = parameter.content();

		// 자식 댓글 권한 확인
		validateChildCommentAuthority(memberId, childCommentId);

		ChildComment childComment = childCommentRepository.findById(childCommentId)
			.orElseThrow(() -> ApiException.from(UNAUTHORIZED_CHILD_COMMENT));

		childComment.updateChildComment(content);
	}

	//자식 댓글 삭제
	@Transactional(rollbackFor = ApiException.class)
	public void validateAndDeleteChildComment(
		ValidAndDeleteParentCommentParameter parameter
	) {
		Long childCommentId = parameter.childCommentId();
		Long memberId = parameter.memberId();

		// 자식 댓글 권한 확인
		validateChildCommentAuthority(memberId, childCommentId);

		ParentComment parentComment = childCommentRepository.findById(childCommentId)
			.orElseThrow(() -> ApiException.from(CHILD_COMMENT_NOT_FOUND))
			.getParentComment();

		// 자식 댓글 삭제
		parentComment.deleteChildComment(childCommentId);
		childCommentRepository.deleteById(childCommentId);

		// 자식 댓글이 없으며 부모 댓글이 삭제된 상태면 삭제
		parentCommentService.deleteIfParentCommentIsDeletedAndChildrenIsEmpty(parentComment);
	}
}
