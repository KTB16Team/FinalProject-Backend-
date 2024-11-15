package aimo.backend.domains.member.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.member.dto.parameter.SignUpParameter;
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

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_image_id", referencedColumnName = "profile_id")
	private ProfileImage profileImage;

	@Column(nullable = false, name = "birth_date")
	private LocalDate birthDate;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PrivatePost> privatePosts = new ArrayList<>();

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


	public static Member of(SignUpParameter signUpParameter, String encodedPassword) {
		return Member.builder()
			.nickname(signUpParameter.nickname())
			.email(signUpParameter.email())
			.password(encodedPassword)
			.memberRole(MemberRole.USER)
			.gender(signUpParameter.gender())
			.provider(Provider.AIMO)
			.birthDate(signUpParameter.birth())
			.profileImage(null)
			.build();
	}


	@Builder
	private Member(
		String nickname,
		String email,
		String password,
		MemberRole memberRole,
		Gender gender,
		Provider provider,
		LocalDate birthDate,
		ProfileImage profileImage,
		List<PrivatePost> privatePosts,
		List<Post> posts,
		List<ParentComment> parentComments,
		List<ChildComment> childComments
	) {
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.role = memberRole;
		this.gender = gender;
		this.provider = provider;
		this.birthDate = birthDate;
		this.profileImage = profileImage;
		this.privatePosts = privatePosts;
		this.posts = posts;
		this.parentComments = parentComments;
		this.childComments = childComments;
	}
}
