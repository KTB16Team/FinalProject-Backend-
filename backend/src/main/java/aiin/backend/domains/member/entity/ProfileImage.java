package aiin.backend.domains.member.entity;

import aiin.backend.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "profile_images")
public class ProfileImage extends BaseEntity {
	@Id
	@Column(name = "profile_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String image_file_url;

	@Column(nullable = false)
	private Long image_file_size;

	@Column(nullable = false)
	private String image_file_name;

	@Column(nullable = false)
	private String image_file_extension;

	@OneToOne(mappedBy = "profileImage")
	private Member member;
}
