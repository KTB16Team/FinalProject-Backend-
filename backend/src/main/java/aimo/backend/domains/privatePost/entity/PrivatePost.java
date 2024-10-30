package aimo.backend.domains.privatePost.entity;

import static lombok.AccessLevel.*;

import aimo.backend.domains.privatePost.model.OriginType;
import aimo.backend.domains.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "disputes")
@NoArgsConstructor(access = PROTECTED)
public class PrivatePost {
	@Id @GeneratedValue
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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "text_record_id", referencedColumnName = "text_record_id", nullable = false)
	private TextRecord textRecord;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OriginType originType;

	@Column(nullable = false)
	private Double faultRate;

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
		OriginType originType,
		Double faultRate)
	{
		this.title = title;
		this.member = member;
		this.stancePlaintiff = stancePlaintiff;
		this.stanceDefendant = stanceDefendant;
		this.summaryAi = summaryAi;
		this.judgement = judgement;
		this.audioRecord = audioRecord;
		this.textRecord = textRecord;
		this.originType = originType;
		this.faultRate = faultRate;
	}
}
