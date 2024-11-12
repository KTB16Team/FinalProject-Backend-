package aimo.backend.domains.like.entity;

import static lombok.AccessLevel.*;

import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
	name = "parent_comment_likes",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"parent_comment_id", "member_id"})
	})
@NoArgsConstructor(access = PROTECTED)
public class ParentCommentLike extends Like {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_comment_id")
	private ParentComment parentComment;

	@Builder
	private ParentCommentLike(Member member, ParentComment parentComment) {
		super(member);
		this.parentComment = parentComment;
	}

	public static ParentCommentLike from(Member member, ParentComment parentComment) {
		return ParentCommentLike.builder().member(member).parentComment(parentComment).build();
	}
}
