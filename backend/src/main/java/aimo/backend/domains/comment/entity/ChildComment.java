package aimo.backend.domains.comment.entity;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.List;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.comment.costants.CommentConstants;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "child_comments")
@NoArgsConstructor(access = PROTECTED)
public class ChildComment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "child_comment_id")
	private Long id;

	@Column(nullable = false)
	private String memberName;

	@Column(nullable = false, length = 500)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_comment_id")
	private ParentComment parentComment;

	@Column(nullable = false)
	private Boolean isDeleted;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@OneToMany(mappedBy = "childComment", fetch = FetchType.LAZY)
	private List<ChildCommentLike> childCommentLikes;

	@Builder
	private ChildComment(
		String memberName,
		String content,
		ParentComment parentComment,
		Boolean isDeleted,
		Member member,
		Post post
	) {
		this.memberName = memberName;
		this.content = content;
		this.parentComment = parentComment;
		this.isDeleted = isDeleted;
		this.member = member;
		this.post = post;
	}

	public Integer getLikesCount() {
		return childCommentLikes.size();
	}

	public void deleteChildCommentSoftly() {
		this.member = null;
		this.memberName = CommentConstants.DELETED_MEMBER.getValue();
		this.isDeleted = true;
	}

	public void deleteChildCommentSoftlyWithContent() {
		this.member = null;
		this.memberName = CommentConstants.DELETED_MEMBER.getValue();
		this.isDeleted = true;
		content = CommentConstants.DELETED_COMMENT.getValue();
	}

	public void updateChildComment(String content) {
		this.content = content;
	}
}
