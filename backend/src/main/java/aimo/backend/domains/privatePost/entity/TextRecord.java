package aimo.backend.domains.privatePost.entity;

import aimo.backend.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "text_records")
public class TextRecord extends BaseEntity {
	@Id @GeneratedValue
	@Column(name = "text_record_id")
	private Long id;

	@Column(nullable = false, length = 10000)
	private String script;

	@OneToOne(mappedBy = "textRecord")
	private PrivatePost privatePost;
}
