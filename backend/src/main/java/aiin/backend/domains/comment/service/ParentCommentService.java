package aiin.backend.domains.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aiin.backend.common.exception.ApiException;
import aiin.backend.domains.comment.dto.request.SaveParentCommentRequest;
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

	// 부모 댓글 저장
	@Transactional(rollbackFor = ApiException.class)
	public void saveParentComment(Long postId, Member member, SaveParentCommentRequest request) {
		Post post = postService.findById(postId);
		ParentComment parentComment = parentCommentMapper.from(request, member, post);

		parentCommentRepository.save(parentComment);
	}
}
