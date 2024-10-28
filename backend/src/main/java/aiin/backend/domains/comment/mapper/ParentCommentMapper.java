package aiin.backend.domains.comment.mapper;

import org.springframework.stereotype.Component;

import aiin.backend.domains.comment.dto.request.SaveParentCommentRequest;
import aiin.backend.domains.comment.entity.ParentComment;
import aiin.backend.domains.member.entity.Member;
import aiin.backend.domains.post.entity.Post;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParentCommentMapper {

	public ParentComment from(
		SaveParentCommentRequest request,
		Member member,
		Post post
	) {
		return ParentComment
			.builder()
			.memberName(member.getUsername())
			.content(request.getContent())
			.isDeleted(false)
			.member(member)
			.post(post)
			.build();
	}
}
