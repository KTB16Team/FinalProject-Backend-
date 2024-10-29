package aiin.backend.domains.dispute.entity;

import static lombok.AccessLevel.*;

import aiin.backend.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audio_records")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AudioRecord extends BaseEntity {
	@Id @GeneratedValue
	@Column(name = "audio_id")
	private Long id;

	@Column(nullable = false, name = "audio_file_url")
	private String url;

	@Column(nullable = false, name = "audio_file_size")
	private String size;

	@Column(nullable = false, name = "audio_file_extension")
	private String extension;

	@OneToOne(mappedBy = "audioRecord")
	private Dispute dispute;
}
