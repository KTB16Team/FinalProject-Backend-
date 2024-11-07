package aimo.backend.domains.privatePost.entity;

import static jakarta.persistence.GenerationType.*;

import aimo.backend.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "text_records")
@NoArgsConstructor
public class TextRecord extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "text_record_id")
	private Long id;

	@Column(nullable = false, length = 10000)
	private String script;

	@OneToOne(mappedBy = "textRecord")
	private PrivatePost privatePost;

	@Builder
	private TextRecord(String script) {
		this.script = script;
	}
}
