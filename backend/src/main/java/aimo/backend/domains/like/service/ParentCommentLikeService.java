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
import aimo.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentCommentLikeService {

	private final ParentCommentLikeRepository parentCommentLikeRepository;
	private final ParentCommentRepository parentCommentRepository;
	private final MemberRepository memberRepository;

	@Transactional(rollbackFor = ApiException.class)
	public void likeParentComment(LikeParentCommentParameter parameter) {
		Long memberId = parameter.memberId();
		Long parentCommentId = parameter.parentCommentId();
		LikeType likeType = parameter.likeType();

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));

		ParentComment parentComment = parentCommentRepository.findById(parentCommentId)
			.orElseThrow(() -> ApiException.from(PARENT_COMMENT_NOT_FOUND));

		if (likeType == LikeType.LIKE) {
			// 라이크가 이미 존재하면 무시
			if (parentCommentLikeRepository.existsByParentCommentIdAndMemberId(parentCommentId, memberId)) return;

			parentCommentLikeRepository.save(ParentCommentLike.from(member, parentComment));
			return;
		}

		parentCommentLikeRepository.deleteByMember_IdAndParentComment_Id(member.getId(), parentCommentId);
	}
}
