package aimo.backend.domains.like.service;

import static aimo.backend.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.domains.like.dto.parameter.LikePostParameter;
import aimo.backend.domains.like.model.LikeType;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.like.entity.PostLike;
import aimo.backend.domains.like.repository.PostLikeRepository;
import aimo.backend.domains.post.service.PostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeMemberService {

	private final PostLikeRepository postLikeRepository;
	private final PostService postService;
	private final MemberRepository memberRepository;

	@Transactional(rollbackFor = Exception.class)
	public void likePost(LikePostParameter parameter) {
		Long postId = parameter.postId(), memberId = parameter.memberId();
		LikeType likeType = parameter.likeType();
		Post post = postService.findById(postId);
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));

		if (likeType == LikeType.LIKE) {
			// 라이크가 이미 존재하면 무시
			if (postLikeRepository.existsByPostIdAndMemberId(postId, memberId))
				return;

			PostLike postLike = PostLike.from(member, post);
			postLikeRepository.save(postLike);
			return ;
		}

		postLikeRepository.deleteByMember_IdAndPost_Id(member.getId(), postId);
	}
}
