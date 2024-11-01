package aimo.backend.domains.comment.mapper;

import org.springframework.stereotype.Component;

import aimo.backend.domains.comment.dto.request.SaveParentCommentRequest;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParentCommentMapper {

	public ParentComment from(SaveParentCommentRequest request, Member member, Post post) {
		return ParentComment.builder()
			.memberName(member.getUsername())
			.content(request.content())
			.isDeleted(false)
			.member(member)
			.post(post)
			.build();
	}
}
