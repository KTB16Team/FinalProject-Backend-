package aimo.backend.common.mapper;

import java.util.List;

import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.dto.requset.SavePostRequest;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse.ParentCommentDto;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.vote.model.Side;


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
			.faultRatePlaintiff(request.faultRatePlaintiff())
			.faultRateDefendant(request.faultRateDefendant())
			.privatePostId(request.privatePostId())
			.originType(request.originType())
			.category(request.category())
			.build();
	}

	public static FindJudgementResponse toJudgement(Post post){
		return new FindJudgementResponse(
			post.getTitle(),
			post.getSummaryAi(),
			post.getStancePlaintiff(),
			post.getStanceDefendant(),
			post.getJudgement(),
			post.getFaultRatePlaintiff(),
			post.getFaultRateDefendant(),
			post.getOriginType());
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

	public static FindCommentedPostsByIdRequest toFindCommentedPostsByIdRequest(ParentComment parentComment) {
		final Post post = parentComment.getPost();
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

		return new FindCommentedPostsByIdRequest(
			post.getId(),
			post.getTitle(),
			getPreview(post.getSummaryAi()),
			post.getPostLikesCount(),
			post.getPostViewsCount(),
			post.getCommentsCount(),
			voteRatePlaintiff,
			voteRateDefendant,
			post.getCreatedAt(),
			parentComment.getCreatedAt()
		);
	}

	public static FindCommentedPostsByIdRequest toFindCommentedPostsByIdRequest(ChildComment childComment) {
		final Post post = childComment.getPost();
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

		return new FindCommentedPostsByIdRequest(
			post.getId(),
			post.getTitle(),
			getPreview(post.getSummaryAi()),
			post.getPostLikesCount(),
			post.getPostViewsCount(),
			post.getCommentsCount(),
			voteRatePlaintiff,
			voteRateDefendant,
			post.getCreatedAt(),
			childComment.getCreatedAt()
		);
	}

	public static FindPostsByPostTypeResponse toFindPostsByPostTypeResponse(FindCommentedPostsByIdRequest findCommentedPostsByIdRequest){
		return new FindPostsByPostTypeResponse(
			findCommentedPostsByIdRequest.id(),
			findCommentedPostsByIdRequest.title(),
			findCommentedPostsByIdRequest.contentPreview(),
			findCommentedPostsByIdRequest.likesCount(),
			findCommentedPostsByIdRequest.viewsCount(),
			findCommentedPostsByIdRequest.commentsCount(),
			findCommentedPostsByIdRequest.voteRatePlaintiff(),
			findCommentedPostsByIdRequest.voteRateDefendant(),
			findCommentedPostsByIdRequest.createdAt()
		);
	}

	public static FindPostAndCommentsByIdResponse toFindPostAndCommentsByIdResponse(
		Member member,
		Post post,
		List<ParentComment> parentComments
	) {
		return new FindPostAndCommentsByIdResponse(
			post.getMember() == member,
			post.getId(),
			post.getPostLikes().stream()
				.anyMatch(postLike -> postLike.getMember() == member),
			post.getVotes().stream()
				.filter(vote -> vote.getMember() == member)
				.findFirst()
				.map(vote -> vote.getSide().getValue())
				.orElse(Side.NONE.getValue()),
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