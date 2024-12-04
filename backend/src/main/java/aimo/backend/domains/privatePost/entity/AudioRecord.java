package aimo.backend.domains.privatePost.entity;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.privatePost.dto.parameter.SaveAudioSuccessParameter;
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
@Table(name = "audio_records")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AudioRecord extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "audio_record_id")
	private Long id;

	@Column(nullable = false, name = "file_name")
	private String filename;

	@Column(nullable = false, name = "url")
	private String url;

	@Column(nullable = false, name = "size")
	private Long size;

	@Column(nullable = false, name = "extension")
	private String extension;

	@OneToOne(mappedBy = "audioRecord")
	private PrivatePost privatePost;

	@Builder
	private AudioRecord(String filename, String extension, String url, Long size) {
		this.filename = filename;
		this.url = url;
		this.size = size;
		this.extension = extension;
	}

	public static AudioRecord from(SaveAudioSuccessParameter parameter) {
		return AudioRecord.builder()
			.filename(parameter.filename())
			.extension(parameter.extension())
			.url(parameter.url())
			.size(parameter.size())
			.build();
	}
}
