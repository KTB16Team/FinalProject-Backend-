package aimo.backend.domains.like.entity;

import static lombok.AccessLevel.*;

import aimo.backend.domains.comment.entity.ChildComment;
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
	name = "child_comment_likes",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"child_comment_id", "member_id"})
	})
@NoArgsConstructor(access = PROTECTED)
public class ChildCommentLike extends Like {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_comment_id")
	private ChildComment childComment;

	@Builder
	private ChildCommentLike(Member member, ChildComment childComment) {
		super(member);
		this.childComment = childComment;
	}

	public static ChildCommentLike from(Member member, ChildComment childComment) {
		return ChildCommentLike.builder().member(member).childComment(childComment).build();
	}
}
