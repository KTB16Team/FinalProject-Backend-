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
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.service.MemberService;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.service.PostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildCommentService {

	private final ChildCommentRepository childCommentRepository;
	private final PostService postService;
	private final ParentCommentMemberService parentCommentMemberService;
	private final MemberService memberService;

	//자식 댓글 권한 확인
	private void validateChildCommentAuthority(Member member, Long childCommentId) throws ApiException {
		Boolean exists = childCommentRepository.existsByIdAndMember(childCommentId, member);

		if (!exists) {
			throw ApiException.from(UNAUTHORIZED_CHILD_COMMENT);
		}
	}

	//자식 댓글 저장
	@Transactional(rollbackFor = ApiException.class)
	public void saveChildComment(SaveChildCommentParameter saveChildCommentParameter) {
		Long postId = saveChildCommentParameter.postId();
		Long parentCommentId = saveChildCommentParameter.parentCommentId();
		String content = saveChildCommentParameter.content();
		Member member = memberService.findMemberById(saveChildCommentParameter.memberId());
		Post post = postService.findById(postId);

		ParentComment parentComment = parentCommentMemberService.findById(parentCommentId);
		ChildComment childComment = ChildComment.of(content, member, parentComment, post);
		childCommentRepository.save(childComment);
	}

	//자식 댓글 수정
	@Transactional(rollbackFor = ApiException.class)
	public void validateAndUpdateChildComment(
		ValidAndUpdateChildCommentParameter validAndUpdateChildCommentParameter) {
		Member member = memberService.findMemberById(validAndUpdateChildCommentParameter.memberId());
		Long childCommentId = validAndUpdateChildCommentParameter.childCommentId();
		String content = validAndUpdateChildCommentParameter.content();

		validateChildCommentAuthority(member, childCommentId);
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

		Member member = memberService.findMemberById(parameter.memberId());

		// 자식 댓글 권한 확인
		validateChildCommentAuthority(member, childCommentId);

		ParentComment parentComment = childCommentRepository.findById(childCommentId)
			.orElseThrow(() -> ApiException.from(CHILD_COMMENT_NOT_FOUND))
			.getParentComment();

		// 자식 댓글 삭제
		childCommentRepository.deleteById(childCommentId);

		// 자식 댓글이 없으며 부모 댓글이 삭제된 상태면 삭제
		parentCommentMemberService.deleteIfParentCommentIsDeletedAndChildrenIsEmpty(parentComment);
	}

	// id로 자식 댓글 조회
	public ChildComment findById(Long childCommentId) {
		return childCommentRepository.findById(childCommentId)
			.orElseThrow(() -> ApiException.from(CHILD_COMMENT_NOT_FOUND));
	}
}
