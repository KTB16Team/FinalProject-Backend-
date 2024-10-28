package aiin.backend.domains.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aiin.backend.common.exception.ApiException;
import aiin.backend.domains.comment.dto.request.SaveChildCommentRequest;
import aiin.backend.domains.comment.entity.ChildComment;
import aiin.backend.domains.comment.entity.ParentComment;
import aiin.backend.domains.comment.mapper.ChildCommentMapper;
import aiin.backend.domains.comment.repository.ChildCommentRepository;
import aiin.backend.domains.member.entity.Member;
import aiin.backend.domains.post.entity.Post;
import aiin.backend.domains.post.service.PostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildCommentService {

	private final ChildCommentRepository childCommentRepository;
	private final ChildCommentMapper childCommentMapper;
	private final PostService postService;
	private final ParentCommentService parentCommentService;

	//자식 댓글 저장
	@Transactional(rollbackFor = ApiException.class)
	public void saveChildComment(Member member, Long postId, Long parentCommentId, SaveChildCommentRequest request) {
		Post post = postService.findById(postId);
		ParentComment parentComment = parentCommentService.findById(parentCommentId);

		ChildComment childComment = childCommentMapper.from(request, member, parentComment, post);

		childCommentRepository.save(childComment);
	}
}
