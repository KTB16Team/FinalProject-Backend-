package aimo.backend.domains.privatePost.entity;

import static lombok.AccessLevel.*;

import aimo.backend.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_records")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatRecord extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_record_id")
	private Long id;

	@Column(nullable = false)
	private String script;

	@Column(nullable = false)
	private String extension;

	@Column(nullable = false)
	private String filename;

	@Builder
	public ChatRecord(String filename, String extension, String script) {
		this.filename = filename;
		this.extension = extension;
		this.script = script;
	}
}
