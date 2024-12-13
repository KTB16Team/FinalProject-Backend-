package aimo.backend.domains.privatePost.entity;

import static lombok.AccessLevel.*;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.privatePost.dto.parameter.JudgementParameter;
import aimo.backend.domains.privatePost.dto.parameter.UpdatePostContentParameter;
import aimo.backend.domains.privatePost.model.ContentLength;
import aimo.backend.domains.privatePost.model.OriginType;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.upload.entity.FileRecord;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "private_posts")
@NoArgsConstructor(access = PROTECTED)
public class PrivatePost extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "private_post_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "stance_plaintiff", length = 2500)
	private String stancePlaintiff;

	@Column(name = "stance_defendant", length = 2500)
	private String stanceDefendant;

	private String title;

	@Column(name = "summary_ai", length = 2500)
	private String summaryAi;

	@Column(length = 2500)
	private String judgement;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_record_id")
	private FileRecord fileRecord;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "text_record_id")
	private TextRecord textRecord;


	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OriginType originType;

	private Integer faultRatePlaintiff;

	private Integer faultRateDefendant;

	@Column(nullable = false)
	private Boolean published;

	@Builder
	private PrivatePost(
		String title,
		Member member,
		String stancePlaintiff,
		String stanceDefendant,
		String summaryAi,
		String judgement,
		FileRecord fileRecord,
		TextRecord textRecord,
		OriginType originType,
		Integer faultRatePlaintiff,
		Integer faultRateDefendant,
		Boolean published) {

		this.title = title;
		this.member = member;
		this.stancePlaintiff = stancePlaintiff;
		this.stanceDefendant = stanceDefendant;
		this.summaryAi = summaryAi;
		this.judgement = judgement;
		this.fileRecord = fileRecord;
		this.textRecord = textRecord;
		this.originType = originType;
		this.faultRatePlaintiff = faultRatePlaintiff;
		this.faultRateDefendant = faultRateDefendant;
		this.published = published;
	}

	public static PrivatePost from(JudgementParameter parameter, Member member, TextRecord textRecord) {
		return PrivatePost.builder()
			.title(parameter.title())
			.member(member)
			.stancePlaintiff(parameter.stancePlaintiff())
			.stanceDefendant(parameter.stanceDefendant())
			.summaryAi(parameter.summary())
			.judgement(parameter.judgement())
			.originType(parameter.originType())
			.faultRatePlaintiff(parameter.faultRatePlaintiff())
			.faultRateDefendant(parameter.faultRateDefendant())
			.textRecord(textRecord)
			.published(false)
			.build();
	}

	public static PrivatePost createWithoutContent(Member member, TextRecord textRecord, OriginType originType) {
		return PrivatePost.builder()
			.member(member)
			.textRecord(textRecord)
			.originType(originType)
			.published(false)
			.build();
	}

	public void updateContent(UpdatePostContentParameter parameter) {
		this.stancePlaintiff = parameter.stancePlaintiff();
		this.stanceDefendant = parameter.stanceDefendant();
		this.title = parameter.title();
		this.summaryAi = parameter.summaryAi();
		this.judgement = parameter.judgement();
		this.faultRatePlaintiff = parameter.faultRatePlaintiff();
		this.faultRateDefendant = parameter.faultRateDefendant();
	}



	public void publish() {
		this.published = true;
	}

	public void unpublish() {
		this.published = false;
	}

	public String getPreview() {
		return summaryAi.length() > ContentLength.PREVIEW_LENGTH.getValue() ?
			summaryAi.substring(0, ContentLength.PREVIEW_LENGTH.getValue()) + "..." : summaryAi;
	}
}
