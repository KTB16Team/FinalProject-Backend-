package aimo.backend.domains.post.entity;

import static jakarta.persistence.CascadeType.*;
import static lombok.AccessLevel.*;

import java.util.List;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.post.model.Category;
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
	private Member member;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = ALL)
	private List<ParentComment> parentComments;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dispute_id")
	private PrivatePost privatePost;

	@Column(nullable = false)
	private String title;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;

	public void setAuthor(Member member) {
		this.member = member;
		member.getPosts().add(this);
	}
}
