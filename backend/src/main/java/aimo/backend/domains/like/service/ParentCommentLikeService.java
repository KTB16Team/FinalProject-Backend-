package aimo.backend.domains.like.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.repository.ParentCommentRepository;
import aimo.backend.domains.like.dto.parameter.LikeParentCommentParameter;
import aimo.backend.domains.like.entity.ParentCommentLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.repository.ParentCommentLikeRepository;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.PointRule;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.member.service.MemberPointService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentCommentLikeService {

	private final ParentCommentLikeRepository parentCommentLikeRepository;
	private final ParentCommentRepository parentCommentRepository;
	private final MemberRepository memberRepository;
	private final MemberPointService memberPointService;

	@Transactional(rollbackFor = ApiException.class)
	public void likeParentComment(LikeParentCommentParameter parameter) {
		Long memberId = parameter.memberId();
		Long parentCommentId = parameter.parentCommentId();
		LikeType likeType = parameter.likeType();

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));

		ParentComment parentComment = parentCommentRepository.findById(parentCommentId)
			.orElseThrow(() -> ApiException.from(PARENT_COMMENT_NOT_FOUND));

		// 라이크 타입이 LIKE인 경우 저장, 이미 라이크가 존재하면 무시
		if (likeType == LikeType.LIKE) {
			// 라이크가 이미 존재하면 무시
			if (parentCommentLikeRepository.existsByParentCommentIdAndMemberId(parentCommentId, memberId)) return;

			// 라이크 저장
			parentCommentLikeRepository.save(ParentCommentLike.from(member, parentComment));
			// 포인트 증가
			memberPointService.increaseMemberPoint(memberId, PointRule.INCREASE_POINT_FROM_LIKE.getPoint());

			return;
		}

		// 라이크 타입이 DISLIKE인 경우 삭제
		parentCommentLikeRepository.deleteByMember_IdAndParentComment_Id(member.getId(), parentCommentId);
	}
}
