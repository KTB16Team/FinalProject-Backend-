package aimo.backend.domains.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.mapper.ParentCommentLikeMapper;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.service.ParentCommentService;
import aimo.backend.domains.like.dto.LikeParentCommentRequest;
import aimo.backend.domains.like.entity.ParentCommentLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.repository.ParentCommentLikeRepository;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentCommentLikeService {

	private final MemberService memberService;
	private final ParentCommentLikeRepository parentCommentLikeRepository;
	private final ParentCommentService parentCommentService;

	@Transactional(rollbackFor = ApiException.class)
	public void likeParentComment(LikeParentCommentRequest likeParentCommentRequest) {
		Long memberId = likeParentCommentRequest.memberId();
		Long parentCommentId = likeParentCommentRequest.parentCommentId();
		LikeType likeType = likeParentCommentRequest.likeType();
		Member member = memberService.findBy(memberId);
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
