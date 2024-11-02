package aimo.backend.common.mapper;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.dto.PostPreviewResponse;
import aimo.backend.domains.post.dto.SavePostRequest;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.entity.Post;

public class PostMapper {

	public static Post toEntity(SavePostRequest request, Member member) {
		return Post
			.builder()
			.member(member)
			.title(request.title())
			.summaryAi(request.summaryAi())
			.stancePlaintiff(request.stancePlaintiff())
			.stanceDefendant(request.stanceDefendant())
			.privatePostId(request.privatePostId())
			.originType(request.originType())
			.category(request.category())
			.build();
	}

	public static PostPreviewResponse toPreviewResponse(Post post) {
		return new PostPreviewResponse(
			post.getId(),
			post.getTitle(),
			getPreview(post.getSummaryAi(), 90),
			post.getPostLikes().size(),
			post.getPostViews().size(),
			post.getCommentsCount(),
			post.getCreatedAt()
		);
	}

	private static String getPreview(String summaryAi, Integer length) {
		return summaryAi.length() > length ?
			summaryAi.substring(0, length) + "..." : summaryAi;
	}

	public static FindPostsByPostTypeResponse toFindPostsByPostTypeResponse(Post post) {
		Float plaintiffVotesCount = (float)post.getPlaintiffVotesCount();
		Float votesCount = (float)post.getVotesCount();
		Float defendantVotesCount = (float)votesCount - plaintiffVotesCount;

		return new FindPostsByPostTypeResponse(
			post.getId(),
			post.getTitle(),
			getPreview(post.getSummaryAi(), 90),
			post.getPostLikes().size(),
			post.getPostViews().size(),
			post.getCommentsCount(),
			plaintiffVotesCount / votesCount,
			defendantVotesCount / votesCount,
			post.getCreatedAt()
		);
	}
}