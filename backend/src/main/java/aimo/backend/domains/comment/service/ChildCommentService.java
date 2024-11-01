package aimo.backend.domains.comment.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.comment.dto.request.SaveChildCommentRequest;
import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.mapper.ChildCommentMapper;
import aimo.backend.domains.comment.repository.ChildCommentRepository;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.service.PostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildCommentService {

	private final ChildCommentRepository childCommentRepository;
	private final ChildCommentMapper childCommentMapper;
	private final PostService postService;
	private final ParentCommentService parentCommentService;

	//자식 댓글 권한 확인
	private void validateChildCommentAuthority(Member member, Long childCommentId) throws ApiException {
		Boolean exists = childCommentRepository.existsByIdAndMember(childCommentId, member);

		if (!exists) {
			throw ApiException.from(UNAUTHORIZED_CHILD_COMMENT);
		}
	}

	//자식 댓글 저장
	@Transactional(rollbackFor = ApiException.class)
	public void saveChildComment(Member member, Long postId, Long parentCommentId, SaveChildCommentRequest request) {
		Post post = postService.findById(postId);
		ParentComment parentComment = parentCommentService.findById(parentCommentId);

		ChildComment childComment = childCommentMapper.from(request, member, parentComment, post);

		childCommentRepository.save(childComment);
	}

	//자식 댓글 수정
	@Transactional(rollbackFor = ApiException.class)
	public void validateAndUpdateChildComment(Member member, Long childCommentId, UpdateChildCommentRequest request) {
		validateChildCommentAuthority(member, childCommentId);

		ChildComment childComment = childCommentRepository.findById(childCommentId)
			.orElseThrow(() -> ApiException.from(UNAUTHORIZED_CHILD_COMMENT));

		childComment.updateChildComment(request.getContent());
	}

	//자식 댓글 삭제
	@Transactional(rollbackFor = ApiException.class)
	public void validateAndDeleteChildComment(Member member, Long childCommentId) {
		validateChildCommentAuthority(member, childCommentId);

		childCommentRepository.findById(childCommentId)
			.ifPresent(ChildComment::deleteChildCommentSoftly);
	}
}
