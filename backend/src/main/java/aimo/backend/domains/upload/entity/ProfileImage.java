package aimo.backend.domains.upload.entity;

import static lombok.AccessLevel.*;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.upload.dto.parameter.SaveProfileImageMetaDataParameter;
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

	@Column
	private String url;

	@Column(name = "profile_image_key", nullable = false, length = 1000)
	private String key;

	@Column(nullable = false)
	private String filename;

	@Column(nullable = false)
	private String extension;

	@OneToOne(mappedBy = "profileImage")
	private Member member;

	@Builder
	protected ProfileImage(String url, String key, String filename, String extension, Member member) {
		this.url = url;
		this.key = key;
		this.filename = filename;
		this.extension = extension;
		this.member = member;
	}

	public static ProfileImage of(SaveProfileImageMetaDataParameter parameter, Member member, String url) {
		return ProfileImage.builder()
			.url(url)
			.key(parameter.key())
			.filename(parameter.filename())
			.extension(parameter.extension())
			.member(member)
			.build();
	}
}
