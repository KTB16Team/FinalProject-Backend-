package aimo.backend.domains.comment.entity;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.comment.model.CommentConstants;
import aimo.backend.domains.like.entity.ChildCommentLike;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
	name = "child_comments",
	indexes = {
		@Index(name = "child_comments_idx_member_id", columnList = "member_id"),
		@Index(name = "child_comments_idx_post_id", columnList = "post_id")
	}
)
@NoArgsConstructor(access = PROTECTED)
public class ChildComment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "child_comment_id")
	private Long id;

	@Column(nullable = false)
	private String nickname;

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

	@OneToMany(mappedBy = "childComment", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {REMOVE})
	private List<ChildCommentLike> childCommentLikes = new ArrayList<>();

	@Builder
	private ChildComment(
		String nickname,
		String content,
		ParentComment parentComment,
		Member member,
		Post post,
		List<ChildCommentLike> childCommentLikes
	) {
		this.nickname = nickname;
		this.content = content;
		this.parentComment = parentComment;
		this.isDeleted = false;
		this.member = member;
		this.post = post;
		this.childCommentLikes = childCommentLikes;
	}

	public static ChildComment of(
		String content,
		Member member,
		ParentComment parentComment,
		Post post
	) {
		return ChildComment.builder()
			.nickname(member.getNickname())
			.content(content)
			.parentComment(parentComment)
			.member(member)
			.post(post)
			.build();
	}

	public Integer getLikesCount() {
		return childCommentLikes.size();
	}

	public void deleteChildCommentSoftly() {
		this.member = null;
		this.nickname = CommentConstants.DELETED_MEMBER.getValue();
		this.isDeleted = true;
	}

	public void updateChildComment(String content) {
		this.content = content;
	}
}
