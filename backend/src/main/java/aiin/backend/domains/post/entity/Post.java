package aiin.backend.domains.post.entity;

import static lombok.AccessLevel.*;

import java.util.List;

import aiin.backend.common.entity.BaseEntity;
import aiin.backend.domains.dispute.entity.Dispute;
import aiin.backend.domains.member.entity.Member;
import aiin.backend.domains.comment.entity.Comment;
import aiin.backend.domains.post.model.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = PROTECTED)
public class Post extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "post_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member author;

	@OneToMany(mappedBy = "post")
	private List<Comment> comments;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dispute_id")
	private Dispute dispute;

	@Column(nullable = false)
	private String title;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;

	public void setAuthor(Member author) {
		this.author = author;
		author.getPosts().add(this);
	}
}
