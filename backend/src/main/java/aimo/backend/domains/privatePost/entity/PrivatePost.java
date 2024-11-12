package aimo.backend.domains.privatePost.entity;

import static lombok.AccessLevel.*;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.privatePost.dto.parameter.JudgementParameter;
import aimo.backend.domains.privatePost.model.OriginType;
import aimo.backend.domains.member.entity.Member;
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

	@Column(name = "stance_plaintiff", nullable = false, length = 2500)
	private String stancePlaintiff;

	@Column(name = "stance_defendant", nullable = false, length = 2500)
	private String stanceDefendant;

	@Column(nullable = false)
	private String title;

	@Column(name = "summary_ai", nullable = false, length = 2500)
	private String summaryAi;

	@Column(nullable = false, length = 2500)
	private String judgement;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "audio_record_id", referencedColumnName = "audio_record_id")
	private AudioRecord audioRecord;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "text_record_id", referencedColumnName = "text_record_id")
	private TextRecord textRecord;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_record_id", referencedColumnName = "chat_record_id")
	private ChatRecord chatRecord;


	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OriginType originType;

	@Column(nullable = false)
	private Integer faultRatePlaintiff;

	@Column(nullable = false)
	private Integer faultRateDefendant;

	@Column(nullable = false)
	private Boolean published;

	public void publish() {
		this.published = true;
	}

	public void unpublish() {
		this.published = false;
	}

	public String getPreview() {
		return summaryAi.length() > PREVIEW_CONTENT_LENGTH.getValue() ?
			summaryAi.substring(0, PREVIEW_CONTENT_LENGTH.getValue()) + "..." : summaryAi;
	}

	public static PrivatePost toEntity(JudgementParameter parameter, Member member, TextRecord textRecord) {
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

	@Builder
	private PrivatePost(
		String title,
		Member member,
		String stancePlaintiff,
		String stanceDefendant,
		String summaryAi,
		String judgement,
		AudioRecord audioRecord,
		TextRecord textRecord,
		ChatRecord chatRecord,
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
		this.audioRecord = audioRecord;
		this.textRecord = textRecord;
		this.chatRecord = chatRecord;
		this.originType = originType;
		this.faultRatePlaintiff = faultRatePlaintiff;
		this.faultRateDefendant = faultRateDefendant;
		this.published = published;
	}
}
