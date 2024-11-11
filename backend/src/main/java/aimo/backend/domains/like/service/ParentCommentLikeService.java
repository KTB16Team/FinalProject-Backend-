package aimo.backend.domains.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.mapper.ParentCommentLikeMapper;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.service.ParentCommentService;
import aimo.backend.domains.like.dto.parameter.LikeParentCommentParameter;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.repository.ParentCommentLikeRepository;
import aimo.backend.domains.member.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentCommentLikeService {

	private final ParentCommentLikeRepository parentCommentLikeRepository;
	private final ParentCommentService parentCommentService;
	private final EntityManager em;

	@Transactional(rollbackFor = ApiException.class)
	public void likeParentComment(LikeParentCommentParameter parameter) {
		Long memberId = parameter.memberId();
		Long parentCommentId = parameter.parentCommentId();
		LikeType likeType = parameter.likeType();
		Member member = em.getReference(Member.class, memberId);
		ParentComment parentComment = parentCommentService.findById(parentCommentId);

		if (likeType == LikeType.LIKE) {
			// 라이크가 이미 존재하면 무시
			if (parentCommentLikeRepository.existsByParentCommentIdAndMemberId(parentCommentId, memberId)) return;
			parentCommentLikeRepository.save(ParentCommentLikeMapper.toEntity(member, parentComment));
			return;
		}

		parentCommentLikeRepository.deleteByMember_IdAndParentComment_Id(member.getId(), parentCommentId);
	}
}
