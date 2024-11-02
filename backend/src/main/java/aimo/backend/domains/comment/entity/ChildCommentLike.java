package aimo.backend.domains.comment.entity;

import static lombok.AccessLevel.*;

import aimo.backend.common.entity.Like;
import aimo.backend.domains.post.entity.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
}
