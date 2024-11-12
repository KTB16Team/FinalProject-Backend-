package aimo.backend.domains.member.entity;

import static lombok.AccessLevel.*;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.member.dto.parameter.SaveFileMetaDataParameter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "profile_images")
@NoArgsConstructor(access = PROTECTED)
public class ProfileImage extends BaseEntity {

	@Id
	@Column(name = "profile_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String url;

	@Column(nullable = false)
	private Long size;

	@Column(nullable = false)
	private String filename;

	@Column(nullable = false)
	private String extension;

	@OneToOne(mappedBy = "profileImage")
	private Member member;

	@Builder
	protected ProfileImage(String url, Long size, String filename, String extension, Member member) {
		this.url = url;
		this.size = size;
		this.filename = filename;
		this.extension = extension;
		this.member = member;
	}

	public static ProfileImage from(SaveFileMetaDataParameter parameter, Member member, String url) {
		return ProfileImage.builder()
			.url(url)
			.size(parameter.size())
			.filename(parameter.filename())
			.extension(parameter.extension())
			.member(member)
			.build();
	}
}
