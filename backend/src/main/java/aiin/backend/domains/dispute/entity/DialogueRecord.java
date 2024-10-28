package aiin.backend.domains.dispute.entity;

import aiin.backend.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "dialogue_records")
public class DialogueRecord extends BaseEntity {
	@Id @GeneratedValue
	@Column(name = "dialogue_id")
	private Long id;

	@Column(nullable = false)
	private String dialogue;

	@OneToOne(mappedBy = "dialogueRecord")
	private Dispute dispute;
}
