package aimo.backend.domains.comment.mapper;

import org.springframework.stereotype.Component;

import aimo.backend.domains.comment.dto.parameter.SaveChildCommentParameter;
import aimo.backend.domains.comment.dto.parameter.ValidAndDeleteParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.ValidAndUpdateChildCommentParameter;
import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChildCommentMapper {

	public static ChildComment toEntity(
		String content,
		Member member,
		ParentComment parentComment,
		Post post) {
		return ChildComment.builder()
			.nickname(member.getNickname())
			.content(content)
			.parentComment(parentComment)
			.isDeleted(false)
			.member(member)
			.post(post)
			.build();
	}

	public static ValidAndUpdateChildCommentParameter toValidAndUpdateChildCommentParameter(
		Long memberId,
		Long childCommentId,
		String content) {
		return new ValidAndUpdateChildCommentParameter(memberId, childCommentId, content);
	}

	public static ValidAndDeleteParentCommentParameter toValidAndDeleteParentCommentParameter(
		Long memberId,
		Long childCommentId) {
		return new ValidAndDeleteParentCommentParameter(memberId, childCommentId);
	}

	public static SaveChildCommentParameter toSaveChildCommentParameter(
		Long memberId,
		Long postId,
		Long parentCommentId,
		String content) {
		return new SaveChildCommentParameter(memberId, postId, parentCommentId, content);
	}
}
