package aimo.backend.domains.comment.entity;

import static lombok.AccessLevel.*;

import java.util.List;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.comment.model.CommentConstants;
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

	@OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
	private List<ParentCommentLike> parentCommentLikes;

	public Integer getChildCommentsCount() {
		return childComments.size();
	}

	public Integer getLikesCount() {
		return parentCommentLikes.size();
	}

	@Builder
	private ParentComment(
		String nickname,
		String content,
		Member member,
		Post post,
		List<ChildComment> childComments,
		List<ParentCommentLike> parentCommentLikes
	) {
		this.nickname = nickname;
		this.content = content;
		this.isDeleted = false;
		this.member = member;
		this.post = post;
		this.childComments = childComments;
		this.parentCommentLikes = parentCommentLikes;
	}

	public static ParentComment of(
		Member member,
		Post post,
		String content
	) {
		return ParentComment.builder()
			.nickname(member.getNickname())
			.content(content)
			.member(member)
			.post(post)
			.build();
	}

	// 부모 댓글 이름 삭제
	public void deleteParentCommentSoftly() {
		this.member = null;
		this.nickname = CommentConstants.UNKNOWN_MEMBER.getValue();
		this.isDeleted = true;
	}

	// 부모 댓글 이름, 내용 삭제
	public void deleteParentCommentSoftlyWithContent() {
		this.member = null;
		this.nickname = CommentConstants.UNKNOWN_MEMBER.getValue();
		this.isDeleted = true;
		content = CommentConstants.DELETED_COMMENT.getValue();
	}

	// 부모 댓글 수정
	public void updateContent(String content) {
		this.content = content;
	}

	// 자식 댓글 삭제
	public void deleteChildComment(Long childCommentId) {
		childComments.removeIf(childComment -> childComment.getId().equals(childCommentId));
	}
}
