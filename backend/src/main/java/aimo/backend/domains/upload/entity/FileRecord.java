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

	@Column(nullable = false, length = 1000)
	private String url;

	@Column(nullable = false)
	private Long size;

	@Column(nullable = false)
	private String extension;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PreSignedUrlPrefix prefix;

	@OneToOne(mappedBy = "fileRecord")
	private PrivatePost privatePost;

	@Builder
	private FileRecord(String filename, String extension, String url, Long size, PreSignedUrlPrefix prefix) {
		this.filename = filename;
		this.url = url;
		this.size = size;
		this.extension = extension;
		this.prefix = prefix;
	}

	public static FileRecord from(SaveFileMetaDataParameter parameter) {
		return FileRecord.builder()
			.filename(parameter.filename())
			.extension(parameter.extension())
			.url(parameter.url())
			.size(parameter.size())
			.prefix(parameter.prefix())
			.build();
	}
}
