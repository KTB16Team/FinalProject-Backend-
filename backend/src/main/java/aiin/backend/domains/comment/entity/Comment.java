package aiin.backend.domains.comment.entity;

import static lombok.AccessLevel.*;

import java.util.List;

import aiin.backend.common.entity.BaseEntity;
import aiin.backend.domains.member.entity.Member;
import aiin.backend.domains.post.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "comment_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private Member author;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@OneToMany(mappedBy = "parent")
	private List<ChildComment> children;

	@Column(nullable = false)
	private String content;

	void setAuthor(Member author) {
		this.author = author;
		author.getComments().add(this);
	}

	void setPost(Post post) {
		this.post = post;
		post.getComments().add(this);
	}
}
