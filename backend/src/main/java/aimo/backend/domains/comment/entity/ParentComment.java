package aimo.backend.domains.comment.entity;

import static lombok.AccessLevel.*;

import java.util.List;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.comment.costants.CommentConstants;
import aimo.backend.domains.like.entity.ParentCommentLike;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "parent_comments")
@NoArgsConstructor(access = PROTECTED)
public class ParentComment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "parent_comment_id")
	private Long id;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false, length = 500)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY)
	private List<ChildComment> childComments;

	@Column(nullable = false)
	private Boolean isDeleted;

	@OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY)
	private List<ParentCommentLike> parentCommentLikes;

	public Integer getCommentsCount() {
		return childComments.size() + 1;
	}

	public Integer getLikesCount() {
		return parentCommentLikes.size();
	}

	@Builder
	private ParentComment(
		String nickname,
		String content,
		Boolean isDeleted,
		Member member,
		Post post
	) {
		this.nickname = nickname;
		this.content = content;
		this.isDeleted = isDeleted;
		this.member = member;
		this.post = post;
	}

	public void deleteChildCommentSoftly() {
		this.member = null;
		this.nickname = CommentConstants.DELETED_COMMENT.getValue();
		this.isDeleted = true;
	}

	public void deleteChildCommentSoftlyWithContent() {
		this.member = null;
		this.nickname = CommentConstants.DELETED_MEMBER.getValue();
		this.isDeleted = true;
		content = CommentConstants.DELETED_COMMENT.getValue();
	}

	public void updateContent(String content) {
		this.content = content;
	}
}
