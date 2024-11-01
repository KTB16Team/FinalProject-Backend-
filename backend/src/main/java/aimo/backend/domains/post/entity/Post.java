package aimo.backend.domains.post.entity;

import static jakarta.persistence.CascadeType.*;
import static lombok.AccessLevel.*;

import java.util.List;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.post.model.Side;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.post.model.Category;
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
import jakarta.persistence.OneToOne;
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
	@GeneratedValue
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

	public void setAuthor(Member member) {
		this.member = member;
		member.getPosts().add(this);
	}

	public Integer getCommentsCount() {
		return parentComments
			.stream()
			.map(ParentComment::getCommentsCount)
			.reduce(1, Integer::sum);
	}

	public void deletePost() {
		this.member.getPosts().remove(this);
		this.member = null;
	}

	public Long getPlaintiffVotesCount() {
		return votes
			.stream()
			.filter(vote -> vote.getSide() == Side.PLAINTIFF)
			.count();
	}

	public Long getVotesCount() {
		return votes
			.stream()
			.count();
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
		OriginType originType,
		Category category) {

		this.privatePostId = privatePostId;
		this.member = member;
		this.title = title;
		this.summaryAi = summaryAi;
		this.judgement = judgement;
		this.stancePlaintiff = stancePlaintiff;
		this.stanceDefendant = stanceDefendant;
		this.originType = originType;
		this.category = category;
	}
}
