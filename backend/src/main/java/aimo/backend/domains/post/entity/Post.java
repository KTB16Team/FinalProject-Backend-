package aimo.backend.domains.post.entity;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.List;
import java.util.Objects;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.like.entity.PostLike;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.model.Category;
import aimo.backend.domains.post.model.Side;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.privatePost.model.OriginType;
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
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = PROTECTED)
public class Post extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = ALL)
	private List<ParentComment> parentComments;

	@JoinColumn(name = "origin_private_post_id")
	private Long privatePostId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 2500)
	private String summaryAi;

	@Column(nullable = false, length = 2500)
	private String stancePlaintiff;

	@Column(nullable = false, length = 2500)
	private String stanceDefendant;

	@Column(nullable = false)
	private Integer faultRatePlaintiff;

	@Column(nullable = false)
	private Integer faultRateDefendant;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OriginType originType;

	@Column(nullable = false, length = 2500)
	private String judgement;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "VARCHAR(100) DEFAULT 'COMMON'")
	private Category category;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = ALL)
	private List<PostLike> postLikes;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = ALL)
	private List<PostView> postViews;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = ALL)
	private List<Vote> votes;

	public void setMember(Member member) {
		this.member = member;
		member.getPosts().add(this);
	}

	public Integer getPostLikesCount() {
		return postLikes.size();
	}

	public Integer getPostViewsCount() {
		return postViews.size();
	}

	public Integer getCommentsCount() {
		return parentComments.size() +
			parentComments.stream()
			.map(ParentComment::getChildCommentsCount)
			.reduce(0, Integer::sum);
	}

	public void delete() {
		this.member.getPosts().remove(this);
		this.member = null;
	}

	public void softDelete() {
		this.member = null;
		this.privatePostId = null;
	}

	public Integer getPlaintiffVotesCount() {
		return votes.stream()
			.filter(vote -> vote.getSide() == Side.PLAINTIFF)
			.toList()
			.size();
	}

	public Integer getDefendantVotesCount() {
		return votes.stream()
			.filter(vote -> vote.getSide() == Side.DEFENDANT)
			.toList()
			.size();
	}

	public Integer getVotesCount() {
		return votes.size();
	}

	@Builder
	public Post(
		Long privatePostId,
		Member member,
		String title,
		String summaryAi,
		String stancePlaintiff,
		String stanceDefendant,
		String judgement,
		Integer faultRatePlaintiff,
		Integer faultRateDefendant,
		OriginType originType,
		Category category) {

		this.privatePostId = privatePostId;
		this.member = member;
		this.title = title;
		this.summaryAi = summaryAi;
		this.judgement = judgement;
		this.stancePlaintiff = stancePlaintiff;
		this.stanceDefendant = stanceDefendant;
		this.faultRateDefendant = faultRateDefendant;
		this.faultRatePlaintiff = faultRatePlaintiff;
		this.originType = originType;
		this.category = category;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Post post = (Post)o;
		return Objects.equals(id, post.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	public void increaseViewsCount(PostView postView) {
		this.postViews.add(postView);
	}
}
