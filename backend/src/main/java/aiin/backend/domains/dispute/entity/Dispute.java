package aiin.backend.domains.dispute.entity;

import static lombok.AccessLevel.*;

import aiin.backend.domains.dispute.model.OriginType;
import aiin.backend.domains.member.entity.Member;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "disputes")
@NoArgsConstructor(access = PROTECTED)
public class Dispute {
	@Id @GeneratedValue
	@Column(name = "dispute_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = true)
	private Member member;

	@Column(name = "stance_plaintiff")
	private String stanceA;

	@Column(name = "stance_defendant")
	private String stanceB;

	@Column(name = "summary_ai")
	private String summaryAi;

	@Column(name = "summary_masking")
	private String summaryMasking;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "origin_audio_id", referencedColumnName = "audio_id")
	private AudioRecord audioRecord;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orgin_dialogue_id", referencedColumnName = "dialogue_id")
	private DialogueRecord dialogueRecord;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OriginType originType;
}
