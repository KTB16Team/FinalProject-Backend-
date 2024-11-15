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
import aimo.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildCommentLikeService {

	private final ChildCommentLikeRepository childCommentLikeRepository;
	private final MemberRepository memberRepository;
	private final ChildCommentRepository childCommentRepository;

	@Transactional(rollbackFor = ApiException.class)
	public void likeChildComment(LikeChildCommentParameter parameter) {
		Long childCommentId = parameter.childCommentId();
		Long memberId = parameter.memberId();

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));
		ChildComment childComment = childCommentRepository.findById(childCommentId)
			.orElseThrow(() -> ApiException.from(CHILD_COMMENT_NOT_FOUND));

		if (parameter.likeType() == LikeType.LIKE) {
			if (childCommentLikeRepository.existsByChildCommentIdAndMemberId(childCommentId, memberId))
				return;

			childCommentLikeRepository.save(ChildCommentLike.from(member, childComment));
			return;
		}

		childCommentLikeRepository.deleteByMemberIdAndChildCommentId(member.getId(), childCommentId);
	}
}
