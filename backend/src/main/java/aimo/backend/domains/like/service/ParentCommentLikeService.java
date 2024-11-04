package aimo.backend.domains.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.service.ParentCommentService;
import aimo.backend.domains.like.entity.ParentCommentLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.repository.ParentCommentLikeRepository;
import aimo.backend.domains.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentCommentLikeService {

	private final ParentCommentLikeRepository parentCommentLikeRepository;
	private final ParentCommentService parentCommentService;

	public void likeParentComment(Member member, Long parentCommentId, LikeType likeType) {
		ParentComment parentComment = parentCommentService.findById(parentCommentId);

		if (likeType == LikeType.LIKE) {
			// 라이크가 이미 존재하면 무시
			if (parentCommentLikeRepository.existsByParentComment_IdAndMember_Id(parentCommentId, member.getId())) {
				return;
			}

			parentCommentLikeRepository.save(ParentCommentLike.builder()
				.parentComment(parentComment)
				.member(member)
				.build());
		} else {
			parentCommentLikeRepository.deleteByMember_IdAndParentComment_Id(member.getId(), parentCommentId);
		}
	}
}
