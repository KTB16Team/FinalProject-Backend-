package aiin.backend.domains.member.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import aiin.backend.domains.dispute.entity.Dispute;
import aiin.backend.domains.member.model.Gender;
import aiin.backend.domains.member.model.MemberRole;
import aiin.backend.domains.member.model.Provider;
import aiin.backend.common.entity.BaseEntity;
import aiin.backend.domains.comment.entity.Comment;
import aiin.backend.domains.post.entity.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseEntity {

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MemberRole role;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Provider provider;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_image_id", referencedColumnName = "profile_id")
	private ProfileImage profileImage;

	@Column(nullable = false, name = "phone_number")
	private String phoneNumber;

	@Column(nullable = false, name = "birth_date")
	private LocalDate birthDate;

	@OneToMany(mappedBy = "member")
	private List<Dispute> disputes;

	@OneToMany(mappedBy = "author")
	private List<Post> posts = new ArrayList<>();

	@OneToMany(mappedBy = "author")
	private List<Comment> comments = new ArrayList<>();

	@Builder
	private Member(String username, String email, String password, MemberRole memberRole, Gender gender, Provider provider, String phoneNumber, LocalDate birthDate) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = memberRole;
		this.phoneNumber = phoneNumber;
		this.birthDate = birthDate;
		this.gender = gender;
		this.provider = provider;
	}
}
