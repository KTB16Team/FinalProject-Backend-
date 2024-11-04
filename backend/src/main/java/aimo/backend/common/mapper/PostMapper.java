package aimo.backend.common.mapper;

import java.util.List;

import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.dto.PostPreviewResponse;
import aimo.backend.domains.post.dto.SavePostRequest;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse.ParentCommentDto;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.entity.Post;

public class PostMapper {

	private final static int PREVIEW_CONTENT_LENGTH = 90;

	public static Post toEntity(SavePostRequest request, Member member) {
		return Post
			.builder()
			.member(member)
			.title(request.title())
			.summaryAi(request.summaryAi())
			.judgement(request.judgement())
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
			getPreview(post.getSummaryAi()),
			post.getPostLikes().size(),
			post.getPostViews().size(),
			post.getCommentsCount(),
			post.getCreatedAt()
		);
	}

	private static String getPreview(String summaryAi) {
		return summaryAi.length() > PREVIEW_CONTENT_LENGTH ?
			summaryAi.substring(0, PREVIEW_CONTENT_LENGTH) + "..." : summaryAi;
	}

	public static FindPostsByPostTypeResponse toFindPostsByPostTypeResponse(Post post) {
		final Float plaintiffVotesCount = (float)post.getPlaintiffVotesCount();
		final Float defendantVotesCount = (float)post.getDefendantVotesCount();
		final Float votesCount = (float)post.getVotesCount();

		// 투표율 계산
		float voteRatePlaintiff = 0f;
		float voteRateDefendant = 0f;
		if (votesCount != 0) {
			voteRatePlaintiff = plaintiffVotesCount / votesCount;
			voteRateDefendant = defendantVotesCount / votesCount;
		}

		return new FindPostsByPostTypeResponse(
			post.getId(),
			post.getTitle(),
			getPreview(post.getSummaryAi()),
			post.getPostLikesCount(),
			post.getPostViewsCount(),
			post.getCommentsCount(),
			voteRatePlaintiff,
			voteRateDefendant,
			post.getCreatedAt()
		);
	}

	public static FindPostAndCommentsByIdResponse toFindPostAndCommentsByIdResponse(
		Member member,
		Post post,
		List<ParentComment> parentComments
	) {
		return new FindPostAndCommentsByIdResponse(
			post.getMember() == member,
			post.getTitle(),
			post.getMember().getNickname(),
			post.getSummaryAi(),
			post.getPostLikesCount(),
			post.getPostViewsCount(),
			post.getCommentsCount(),
			post.getVotesCount(),
			post.getPlaintiffVotesCount(),
			post.getDefendantVotesCount(),
			post.getCreatedAt(),
			parentComments.stream()
				.map(parentComment -> ParentCommentDto.of(member, parentComment))
				.toList()
		);
	}
}