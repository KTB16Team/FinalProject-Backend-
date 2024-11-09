package aimo.backend.domains.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.mapper.ChildCommentLikeMapper;
import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.service.ChildCommentService;
import aimo.backend.domains.like.dto.LikeChildCommentRequest;
import aimo.backend.domains.like.entity.ChildCommentLike;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.like.repository.ChildCommentLikeRepository;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildCommentLikeService {

	private final MemberService memberService;
	private final ChildCommentLikeRepository childCommentLikeRepository;
	private final ChildCommentService childCommentService;

	@Transactional(rollbackFor = ApiException.class)
	public void likeChildComment(LikeChildCommentRequest likeChildCommentRequest) {
		Long childCommentId = likeChildCommentRequest.childCommentId();
		Long memberId = likeChildCommentRequest.memberId();
		Member member = memberService.findBy(memberId);

		ChildComment childComment = childCommentService.findById(childCommentId);

		if (likeChildCommentRequest.likeType() == LikeType.LIKE) {
			// 라이크가 이미 존재하면 무시
			if (childCommentLikeRepository.existsByChildCommentIdAndMemberId(childCommentId, memberId)) return;
			childCommentLikeRepository.save(ChildCommentLikeMapper.toChildCommentLike(member, childComment));
			return;
		}

		childCommentLikeRepository.deleteByMemberIdAndChildCommentId(member.getId(), childCommentId);
	}
}
