package aimo.backend.domains.comment.mapper;

import org.springframework.stereotype.Component;

import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.comment.dto.parameter.DeleteParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.SaveParentCommentParameter;
import aimo.backend.domains.comment.dto.parameter.UpdateParentCommentParameter;
import aimo.backend.domains.comment.dto.request.SaveParentCommentRequest;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParentCommentMapper {

	public static SaveParentCommentParameter toSaveParentCommentParameter(Long postId, String content) {
		Long memberId = MemberLoader.getMemberId();
		return new SaveParentCommentParameter(memberId, postId, content);
	}

	public static UpdateParentCommentParameter toUpdateParentCommentParameter(Long postId, String content) {
		Long memberId = MemberLoader.getMemberId();
		return new UpdateParentCommentParameter(memberId, postId, content);
	}

	public static DeleteParentCommentParameter toDeleteParentCommentParameter(Long postId) {
		Long memberId = MemberLoader.getMemberId();
		return new DeleteParentCommentParameter(memberId, postId);
	}

	public ParentComment toEntity(Member member, Post post, String content) {
		return ParentComment.builder()
			.nickname(member.getNickname())
			.content(content)
			.isDeleted(false)
			.member(member)
			.post(post)
			.build();
	}
}
