package aimo.backend.common.mapper;

import java.util.List;

import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.comment.entity.ParentComment;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.post.dto.parameter.DeletePostParameter;
import aimo.backend.domains.post.dto.parameter.FindPostAndCommentsByIdParameter;
import aimo.backend.domains.post.dto.parameter.FindPostByPostTypeParameter;
import aimo.backend.domains.post.dto.parameter.SavePostParameter;
import aimo.backend.domains.post.dto.requset.FindCommentedPostsByIdRequest;
import aimo.backend.domains.post.dto.requset.SavePostRequest;
import aimo.backend.domains.post.dto.response.FindJudgementResponse;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse;
import aimo.backend.domains.post.dto.response.FindPostAndCommentsByIdResponse.ParentCommentDto;
import aimo.backend.domains.post.dto.response.FindPostsByPostTypeResponse;
import aimo.backend.domains.post.dto.response.SavePostResponse;
import aimo.backend.domains.post.entity.Post;
import aimo.backend.domains.post.model.PostType;
import aimo.backend.domains.vote.model.Side;

public class PostMapper {

	private final static int PREVIEW_CONTENT_LENGTH = 90;

	public static Post toEntity(SavePostParameter parameter, Member member) {
		return Post
			.builder()
			.member(member)
			.title(parameter.title())
			.summaryAi(parameter.summaryAi())
			.judgement(parameter.judgement())
			.stancePlaintiff(parameter.stancePlaintiff())
			.stanceDefendant(parameter.stanceDefendant())
			.faultRatePlaintiff(parameter.faultRatePlaintiff())
			.faultRateDefendant(parameter.faultRateDefendant())
			.privatePostId(parameter.privatePostId())
			.originType(parameter.originType())
			.category(parameter.category())
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
		List<ParentComment> parentComments) {
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

	public static SavePostParameter toSavePostParameter(SavePostRequest request) {
		Long memberId = MemberLoader.getMemberId();
		return new SavePostParameter(
			memberId,
			request.privatePostId(),
			request.title(),
			request.stancePlaintiff(),
			request.stanceDefendant(),
			request.summaryAi(),
			request.judgement(),
			request.faultRateDefendant(),
			request.faultRatePlaintiff(),
			request.originType(),
			request.category()
		);
	}

	public static SavePostResponse toSavePostResponse(Long postId) {
		return new SavePostResponse(postId);
	}

	public static FindPostAndCommentsByIdParameter toFindPostAndCommentsByIdParameter(Long postId) {
		return new FindPostAndCommentsByIdParameter(MemberLoader.getMemberId(), postId);
	}

	public static FindPostByPostTypeParameter toFindPostByPostTypeParameter(PostType postType, Integer page, Integer size) {
		Long memberId = MemberLoader.getMemberId();
		return new FindPostByPostTypeParameter(memberId, postType, page, size);
	}

	public static DeletePostParameter toDeletePostParameter(Long postId) {
		Long memberId = MemberLoader.getMemberId();
		return new DeletePostParameter(memberId, postId);
	}
}