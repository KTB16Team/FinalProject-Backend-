package aimo.backend.domains.comment.dto.request;

import aimo.backend.domains.comment.dto.parameter.DeleteParentCommentParameter;

public record DeleteParentCommentRequest(Long commentId) {

	public static DeleteParentCommentRequest of(Long commentId) {
		return new DeleteParentCommentRequest(commentId);
	}

}
