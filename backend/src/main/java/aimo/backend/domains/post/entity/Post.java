package aimo.backend.domains.post.entity;

import static aimo.backend.domains.privatePost.model.ContentLength.*;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.like.entity.PostLike;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.dto.parameter.SavePostParameter;
import aimo.backend.domains.post.model.Category;
import aimo.backend.domains.vote.entity.Vote;
import aimo.backend.domains.vote.model.Side;
import aimo.backend.domains.privatePost.model.OriginType;
import aimo.backend.domains.view.entity.PostView;
import jakarta.persistence.CascadeType;
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

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.REMOVE})
	private List<PostLike> postLikes = new ArrayList<>();

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.REMOVE})
	private List<PostView> postViews = new ArrayList<>();

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.REMOVE})
	private List<Vote> votes = new ArrayList<>();

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
		return parentComments
			.stream()
			.map(ParentComment::getChildCommentsCount)
			.reduce(1, Integer::sum);
	}

	public String getPreview(){
		return summaryAi.length() > PREVIEW_CONTENT_LENGTH.getValue() ?
			summaryAi.substring(0, PREVIEW_CONTENT_LENGTH.getValue()) + "..." : summaryAi;
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
			.mapToInt(vote -> 1)
			.sum();
	}

	public Integer getDefendantVotesCount() {
		return votes.stream()
			.filter(vote -> vote.getSide() == Side.DEFENDANT)
			.mapToInt(vote -> 1)
			.sum();
	}

	public Integer getVotesCount() {
		return votes.size();
	}

	public static Post from(SavePostParameter parameter, Member member) {
		return Post.builder()
			.member(member)
			.title(parameter.title())
			.summaryAi(parameter.summaryAi())
			.stancePlaintiff(parameter.stancePlaintiff())
			.stanceDefendant(parameter.stanceDefendant())
			.faultRatePlaintiff(parameter.faultRatePlaintiff())
			.faultRateDefendant(parameter.faultRateDefendant())
			.judgement(parameter.judgement())
			.originType(parameter.originType())
			.category(parameter.category())
			.build();
	}

	@Builder
	public Post(
		Long privatePostId,
		Member member,
		String title,
		String summaryAi,
		String stancePlaintiff,
		String stanceDefendant,
		Integer faultRatePlaintiff,
		Integer faultRateDefendant,
		String judgement,
		OriginType originType,
		Category category,
		List<ParentComment> parentComments,
		List<PostLike> postLikes,
		List<PostView> postViews,
		List<Vote> votes) {

		this.privatePostId = privatePostId;
		this.member = member;
		this.title = title;
		this.summaryAi = summaryAi;
		this.judgement = judgement;
		this.stancePlaintiff = stancePlaintiff;
		this.stanceDefendant = stanceDefendant;
		this.faultRatePlaintiff = faultRatePlaintiff;
		this.faultRateDefendant = faultRateDefendant;
		this.originType = originType;
		this.category = category;
		this.parentComments = parentComments;
		this.postLikes = postLikes;
		this.postViews = postViews;
		this.votes = votes;
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
