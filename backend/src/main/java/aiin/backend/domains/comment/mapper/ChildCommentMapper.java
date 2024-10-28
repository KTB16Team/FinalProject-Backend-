package aiin.backend.domains.comment.mapper;

import org.springframework.stereotype.Component;

import aiin.backend.domains.comment.dto.request.SaveChildCommentRequest;
import aiin.backend.domains.comment.entity.ChildComment;
import aiin.backend.domains.comment.entity.ParentComment;
import aiin.backend.domains.member.entity.Member;
import aiin.backend.domains.post.entity.Post;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChildCommentMapper {

	public ChildComment from(
		SaveChildCommentRequest request,
		Member member,
		ParentComment parentComment,
		Post post
	) {
		return ChildComment
			.builder()
			.memberName(member.getUsername())
			.content(request.getContent())
			.parentComment(parentComment)
			.isDeleted(false)
			.member(member)
			.post(post)
			.build();
	}
}
