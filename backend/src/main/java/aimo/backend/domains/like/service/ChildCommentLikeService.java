package aimo.backend.domains.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.mapper.ChildCommentLikeMapper;
import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.service.ChildCommentService;
import aimo.backend.domains.like.dto.parameter.LikeChildCommentParameter;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.repository.ChildCommentLikeRepository;
import aimo.backend.domains.member.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildCommentLikeService {

	private final ChildCommentLikeRepository childCommentLikeRepository;
	private final ChildCommentService childCommentService;
	private final EntityManager em;

	@Transactional(rollbackFor = ApiException.class)
	public void likeChildComment(LikeChildCommentParameter parameter) {
		Long childCommentId = parameter.childCommentId();
		Long memberId = parameter.memberId();
		Member member = em.getReference(Member.class, memberId);

		ChildComment childComment = childCommentService.findById(childCommentId);

		if (parameter.likeType() == LikeType.LIKE) {
			// 라이크가 이미 존재하면 무시
			if (childCommentLikeRepository.existsByChildCommentIdAndMemberId(childCommentId, memberId)) return;
			childCommentLikeRepository.save(ChildCommentLikeMapper.toChildCommentLike(member, childComment));
			return;
		}

		childCommentLikeRepository.deleteByMemberIdAndChildCommentId(member.getId(), childCommentId);
	}
}
