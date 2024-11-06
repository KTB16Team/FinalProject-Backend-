package aimo.backend.domains.member.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.member.model.Gender;
import aimo.backend.domains.member.model.MemberRole;
import aimo.backend.domains.member.model.Provider;
import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.post.entity.Post;
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
	private String nickname;

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

	@Column(nullable = false, name = "birth_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	@OneToMany(mappedBy = "member")
	private List<PrivatePost> privatePosts;

	@OneToMany(mappedBy = "member")
	private List<Post> posts = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<ParentComment> parentComments = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<ChildComment> childComments = new ArrayList<>();

	public void updateProfileImage(ProfileImage profileImage) {
		this.profileImage = profileImage;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}



	@Builder
	private Member(
		String nickname,
		String email,
		String password,
		MemberRole memberRole,
		Gender gender,
		Provider provider,
		LocalDate birthDate) {

		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.role = memberRole;
		this.birthDate = birthDate;
		this.gender = gender;
		this.provider = provider;
	}
}
