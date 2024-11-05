package aimo.backend.domains.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.service.ChildCommentService;
import aimo.backend.domains.like.entity.ChildCommentLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.repository.ChildCommentLikeRepository;
import aimo.backend.domains.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildCommentLikeService {

	private final ChildCommentLikeRepository childCommentLikeRepository;
	private final ChildCommentService childCommentService;

	public void likeChildComment(Member member, Long childCommentId, LikeType likeType) {
		ChildComment childComment = childCommentService.findById(childCommentId);

		if (likeType == LikeType.LIKE) {
			// 라이크가 이미 존재하면 무시
			if (childCommentLikeRepository.existsByChildCommentIdAndMemberId(childCommentId, member.getId())) {
				return;
			}

			childCommentLikeRepository.save(ChildCommentLike.builder()
				.childComment(childComment)
				.member(member)
				.build());
		} else {
			childCommentLikeRepository.deleteByMemberIdAndChildCommentId(member.getId(), childCommentId);
		}
	}
}
