package aimo.backend.domains.privatePost.entity;

import static jakarta.persistence.GenerationType.*;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.privatePost.dto.parameter.TextRecordParameter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	@Setter
	private String content;

	@OneToOne(mappedBy = "textRecord")
	private PrivatePost privatePost;

	@Builder
	private TextRecord(String content, PrivatePost privatePost) {
		this.content = content;
		this.privatePost = privatePost;
	}

	public static TextRecord from(TextRecordParameter parameter) {
		return TextRecord.builder()
			.content(parameter.content())
			.build();
	}

	public static TextRecord of(String content) {
		return TextRecord.builder()
			.content(content)
			.build();
	}

	public static TextRecord withoutContent() {
		return TextRecord.builder()
			.content("")
			.build();
	}
}
