package aimo.backend.domains.like.entity;

import static lombok.AccessLevel.*;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.entity.Post;
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
	name = "post_likes",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"post_id", "member_id"})
	})
@NoArgsConstructor(access = PROTECTED)
public class PostLike extends Like {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@Builder
	private PostLike(Post post, Member member) {
		super(member);
		this.post = post;
	}
}
