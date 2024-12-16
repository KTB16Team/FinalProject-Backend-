package aimo.backend.domains.upload.entity;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.upload.dto.parameter.SaveFileMetaDataParameter;
import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file_records")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FileRecord extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "file_record_id")
	private Long id;

	@Column(nullable = false)
	private String filename;

	@Column(name = "file_key", nullable = false, length = 1000)
	private String key;

	@Column(nullable = false)
	private String extension;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PreSignedUrlPrefix prefix;

	@OneToOne(mappedBy = "fileRecord")
	private PrivatePost privatePost;

	@Builder
	private FileRecord(String filename, String extension, String key, PreSignedUrlPrefix prefix) {
		this.filename = filename;
		this.key = key;
		this.extension = extension;
		this.prefix = prefix;
	}

	public static FileRecord of(SaveFileMetaDataParameter parameter, String key) {
		return FileRecord.builder()
			.filename(parameter.filename())
			.extension(parameter.extension())
			.key(key)
			.prefix(parameter.prefix())
			.build();
	}
}
