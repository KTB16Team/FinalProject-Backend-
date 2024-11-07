package aimo.backend.common.config;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.comment.entity.ChildComment;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.comment.repository.ChildCommentRepository;
import aimo.backend.domains.comment.repository.ParentCommentRepository;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.Gender;
import aimo.backend.domains.member.model.MemberRole;
import aimo.backend.domains.member.model.Provider;
import aimo.backend.domains.member.repository.MemberRepository;

import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.model.Category;
import aimo.backend.domains.post.repository.PostRepository;

import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.privatePost.entity.TextRecord;
import aimo.backend.domains.privatePost.model.OriginType;
import aimo.backend.domains.privatePost.repository.PrivatePostRepository;
import aimo.backend.domains.privatePost.repository.TextRecordRepository;

@Configuration
public class DataInitConfig {

	private final PasswordEncoder passwordEncoder;

	public DataInitConfig(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	CommandLineRunner initData(MemberRepository memberRepo, PrivatePostRepository privatePostRepo, PostRepository postRepo,
		ParentCommentRepository parentCommentRepo, ChildCommentRepository childCommentRepo, TextRecordRepository textRecordRepo) {
		return args -> {
			initializeData(memberRepo, privatePostRepo, postRepo, parentCommentRepo, childCommentRepo, textRecordRepo);
		};
	}

	@Transactional
	public void initializeData(MemberRepository memberRepo, PrivatePostRepository privatePostRepo, PostRepository postRepo,
		ParentCommentRepository parentCommentRepo, ChildCommentRepository childCommentRepo, TextRecordRepository textRecordRepo) {
		// Members 생성
		List<Member> members = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			Member member = Member.builder()
				.nickname("nickname" + i)
				.email("a" + i + "@naver.com")
				.password(passwordEncoder.encode("a" + i))
				.memberRole(MemberRole.USER)
				.gender(Gender.MALE)
				.provider(Provider.AIMO)
				.birthDate(LocalDate.of(1990, i, i))
				.build();
			members.add(memberRepo.save(member));
		}

		List<TextRecord> textRecords = new ArrayList<>();

		for(int i=1; i<=100; i++){
			TextRecord textRecord = TextRecord.builder()
				.script("This is a text record " + i)
				.build();
			textRecords.add(textRecordRepo.save(textRecord));
		}

		// PrivatePosts 생성
		List<PrivatePost> privatePosts = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			Member member = members.get(i % 3);
			PrivatePost privatePost =
				PrivatePost.builder()
					.title("Private Post Title " + i)
					.member(member)
					.stancePlaintiff("Plaintiff Stance " + i)
					.stanceDefendant("Defendant Stance " + i)
					.summaryAi("Summary AI " + i)
					.judgement("Judgement " + i)
					.originType(OriginType.TEXT)
					.textRecord(textRecords.get(i-1))
					.faultRatePlaintiff(50)
					.faultRateDefendant(50)
					.published(true)
					.build();

			privatePosts.add(privatePostRepo.save(privatePost));
		}

		// Posts 생성
		List<Post> posts = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			Member member = members.get(i % 3);
			PrivatePost privatePost = privatePosts.get(i - 1);
			Post post = new Post(privatePost.getId(), member, "Public Post Title " + i, "Summary AI " + i,
				"Plaintiff Stance " + i, "Defendant Stance " + i, "Judgement " + i, OriginType.TEXT, Category.COMMON);
			posts.add(postRepo.save(post));
		}

		// ParentComments 생성
		List<ParentComment> parentComments = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			Member member = members.get(i % 3);
			Post post = posts.get(i - 1);
			ParentComment parentComment =
				ParentComment.builder()
					.nickname("ParentComment" + i)
					.content("This is a parent comment " + i)
					.isDeleted(false)
					.member(member)
					.post(post)
					.build();
			parentComments.add(parentCommentRepo.save(parentComment));
		}

		// ChildComments 생성
		for (int i = 1; i <= 10; i++) {
			ParentComment parentComment = parentComments.get(i - 1);
			Member member = members.get(i % 3);
			ChildComment childComment =
				ChildComment.builder()
					.content("This is a child comment " + i)
					.member(member)
					.nickname(member.getNickname())
					.isDeleted(false)
					.parentComment(parentComment)
					.build();
			childCommentRepo.save(childComment);
		}
	}
}