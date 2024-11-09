package aimo.backend.domains.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.repository.ChildCommentRepository;
import aimo.backend.domains.comment.repository.ParentCommentRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostCommentService {

	private final ParentCommentRepository parentCommentRepository;
	private final ChildCommentRepository childCommentRepository;

	// 멤버가 쓴 자식 댓글 조회
	public List<ChildComment> findChildCommentsByMemberId(Long memberId) {
		return childCommentRepository.findByMemberId(memberId);
	}

	// 멤버가 쓴 부모 댓글 조회
	public List<ParentComment> findParentCommentsByMemberId(Long memberId) {
		return parentCommentRepository.findByMemberId(memberId);
	}

	// 부모 댓글 목록을 postId로 조회
	public List<ParentComment> findParentCommentsByPostId(Long postId) {
		return parentCommentRepository.findByPost_Id(postId);
	}
}
