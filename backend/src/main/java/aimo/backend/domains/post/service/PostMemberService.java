package aimo.backend.domains.post.service;

import static aimo.backend.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;

import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.post.dto.parameter.FindPostAndCommentsByIdParameter;
import aimo.backend.domains.post.dto.parameter.SavePostParameter;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.repository.PostRepository;
import aimo.backend.domains.privatePost.service.PrivatePostMemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostMemberService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final PrivatePostMemberService privatePostMemberService;

	@Transactional
	public Long save(SavePostParameter savePostParameter) {
		Member member = memberRepository.findById(savePostParameter.memberId())
			.orElseThrow(() -> ApiException.from(MEMBER_NOT_FOUND));

		privatePostMemberService.publishPrivatePost(savePostParameter.privatePostId());
		Post post = Post.of(savePostParameter, member);
		return postRepository.save(post).getId();
	}

	// 글 조회, dto로 응답
	public FindPostAndCommentsByIdResponse findPostAndCommentsDtoById(FindPostAndCommentsByIdParameter parameter) {
		Post post = postRepository
			.findById(parameter.postId())
			.orElseThrow(() -> ApiException.from(POST_NOT_FOUND));

		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.
				from(MEMBER_NOT_FOUND));

		List<ParentComment> parentComments = post.getParentComments();
		return FindPostAndCommentsByIdResponse.from(member, post, parentComments);
	}

}
