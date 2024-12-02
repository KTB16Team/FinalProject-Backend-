package aimo.backend.domains.post.entity;

import static aimo.backend.domains.privatePost.model.ContentLength.*;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.BatchSize;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.like.entity.PostLike;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.dto.parameter.SavePostParameter;
import aimo.backend.domains.post.model.Category;
import aimo.backend.domains.privatePost.model.OriginType;
import aimo.backend.domains.vote.entity.Vote;
import aimo.backend.domains.vote.model.Side;
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
	@BatchSize(size = 10)
	private List<ParentComment> parentComments = new ArrayList<>();

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = ALL)
	@BatchSize(size = 10)
	private List<ChildComment> childComments = new ArrayList<>();

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

	@Column(nullable = false)	@Enumerated(EnumType.STRING)
	private OriginType originType;

	@Column(nullable = false, length = 2500)
	private String judgement;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "VARCHAR(100) DEFAULT 'COMMON'")
	private Category category;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer postLikesCount = 0;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer postViewsCount = 0;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.REMOVE})
	@BatchSize(size = 10)
	private List<Vote> votes = new ArrayList<>();

	// 편의 메서드
	public void setMember(Member member) {
		this.member = member;
		member.getPosts().add(this);
	}

	// 본문 미리보기
	public String getPreview(){
		return summaryAi.length() > PREVIEW_LENGTH.getValue() ?
			summaryAi.substring(0, PREVIEW_LENGTH.getValue()) + "..." : summaryAi;
	}

	public void softDelete() {
		this.member = null;
		this.privatePostId = null;
	}

	// 원고 투표수 조회
	public Integer getPlaintiffVotesCount() {
		return votes.stream()
			.filter(vote -> vote.getSide() == Side.PLAINTIFF)
			.mapToInt(vote -> 1)
			.sum();
	}

	// 피고 투표수 조회
	public Integer getDefendantVotesCount() {
		return votes.stream()
			.filter(vote -> vote.getSide() == Side.DEFENDANT)
			.mapToInt(vote -> 1)
			.sum();
	}

	// 투표 수 조회
	public Integer getVotesCount() {
		return votes.size();
	}

	// 댓글 수
	public Integer getCommentsCount() {
		return parentComments.size() + childComments.size();
	}


	// 좋아요 수 증가 or 감소
	public void addPostLikesCount(int addCount) {
		int result = this.postLikesCount + addCount;

		if (result < 0) {
			this.postLikesCount = 0;
			return;
		}

		this.postLikesCount = result;
	}

	// 조회 수 증가
	public void addPostViewsCount(int addCount) {
		this.postViewsCount+=addCount;
	}

	public static Post of(SavePostParameter parameter, Member member) {
		return Post.builder()
			.member(member)
			.title(parameter.title())
			.privatePostId(parameter.privatePostId())
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
	private Post(
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
		List<PostLike> postLikes,
		List<Vote> votes
	) {
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
}
