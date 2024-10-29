package aiin.backend.domains.comment.entity;

import static lombok.AccessLevel.*;

import java.util.List;

import aiin.backend.common.entity.BaseEntity;
import aiin.backend.domains.comment.costants.CommentConstants;
import aiin.backend.domains.member.entity.Member;
import aiin.backend.domains.post.entity.Post;
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
	private String memberName;

	@Column(nullable = false, length = 500)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private List<ChildComment> childComments;

	@Column(nullable = false)
	private Boolean isDeleted;

	@Builder
	private ParentComment(
		String memberName,
		String content,
		Boolean isDeleted,
		Member member,
		Post post
	) {
		this.memberName = memberName;
		this.content = content;
		this.isDeleted = isDeleted;
		this.member = member;
		this.post = post;
	}

	public void deleteChildCommentSoftly() {
		this.member = null;
		this.memberName = CommentConstants.DELETED_COMMENT.getValue();
		this.isDeleted = true;
	}

	public void deleteChildCommentSoftlyWithContent() {
		this.member = null;
		this.memberName = CommentConstants.DELETED_MEMBER.getValue();
		this.isDeleted = true;
		content = CommentConstants.DELETED_COMMENT.getValue();
	}

	public void updateContent(String content) {
		this.content = content;
	}
}
