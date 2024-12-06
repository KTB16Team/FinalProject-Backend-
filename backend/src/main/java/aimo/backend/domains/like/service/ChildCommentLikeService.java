package aimo.backend.domains.like.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.repository.ChildCommentRepository;
import aimo.backend.domains.like.dto.parameter.LikeChildCommentParameter;
import aimo.backend.domains.like.entity.ChildCommentLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.repository.ChildCommentLikeRepository;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.MemberPoint;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.member.service.MemberPointService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildCommentLikeService {

	private final ChildCommentLikeRepository childCommentLikeRepository;
	private final MemberRepository memberRepository;
	private final ChildCommentRepository childCommentRepository;
	private final MemberPointService memberPointService;

	@Transactional(rollbackFor = ApiException.class)
	public void likeChildComment(LikeChildCommentParameter parameter) {
		Long childCommentId = parameter.childCommentId();
		Long memberId = parameter.memberId();

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));
		ChildComment childComment = childCommentRepository.findById(childCommentId)
			.orElseThrow(() -> ApiException.from(CHILD_COMMENT_NOT_FOUND));

		// 좋아요 타입이 LIKE인 경우, 이미 좋아요 누르지 않았을 경우에만 좋아요 저장
		if (parameter.likeType() == LikeType.LIKE) {
			if (childCommentLikeRepository.existsByChildCommentIdAndMemberId(childCommentId, memberId))
				return;

			// 좋아요 저장
			childCommentLikeRepository.save(ChildCommentLike.from(member, childComment));
			// 포인트 증가
			memberPointService.increaseMemberPoint(memberId, MemberPoint.INCREASE_POINT_FROM_LIKE.getPoint());
			return;
		}

		// 좋아요 타입이 DISLIKE인 경우, 좋아요 삭제
		childCommentLikeRepository.deleteByMemberIdAndChildCommentId(member.getId(), childCommentId);
	}
}
