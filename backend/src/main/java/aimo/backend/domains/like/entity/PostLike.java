package aimo.backend.domains.like.entity;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
	},
	indexes = {
		@Index(name = "post_likes_idx", columnList = "post_id, member_id")
	}
)
@NoArgsConstructor(access = PROTECTED)
public class PostLike {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "like_id")
	private Long id;

	private Long postId;

	private Long memberId;

	@Builder
	private PostLike (Long postId, Long memberId) {
		this.postId = postId;
		this.memberId = memberId;
	}
}
