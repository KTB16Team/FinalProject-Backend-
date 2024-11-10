package aimo.backend.common.mapper;

import org.springframework.data.domain.Pageable;

import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.privatePost.dto.parameter.DeletePrivatePostParameter;
import aimo.backend.domains.privatePost.dto.parameter.FindPrivatePostParameter;
import aimo.backend.domains.privatePost.dto.parameter.FindPrivatePostPreviewParameter;
import aimo.backend.domains.privatePost.dto.parameter.JudgementParameter;
import aimo.backend.domains.privatePost.dto.parameter.JudgementToAiParameter;
import aimo.backend.domains.privatePost.dto.request.DeletePrivatePostRequest;
import aimo.backend.domains.privatePost.dto.request.FindPrivatePostRequest;
import aimo.backend.domains.privatePost.dto.request.JudgementToAiRequest;
import aimo.backend.domains.privatePost.dto.request.SummaryAndJudgementRequest;
import aimo.backend.domains.privatePost.dto.response.JudgementFromAiResponse;
import aimo.backend.domains.privatePost.dto.response.JudgementResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostResponse;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class PrivatePostMapper {

	public static PrivatePost toEntity(JudgementParameter parameter, Member member) {
		return PrivatePost.builder()
			.title(parameter.title())
			.member(member)
			.stancePlaintiff(parameter.stancePlaintiff())
			.stanceDefendant(parameter.stanceDefendant())
			.summaryAi(parameter.summary())
			.judgement(parameter.judgement())
			.faultRatePlaintiff(parameter.faultRatePlaintiff())
			.faultRateDefendant(parameter.faultRateDefendant())
			.originType(parameter.originType())
			.published(false)
			.build();
	}

	public static PrivatePostPreviewResponse toPreviewResponse(PrivatePost privatePost) {
		return new PrivatePostPreviewResponse(privatePost.getId(), privatePost.getTitle(),
			getPreview(privatePost.getSummaryAi(), 21), privatePost.getOriginType(), privatePost.getCreatedAt(),
			privatePost.getPublished());
	}

	public static String getPreview(String summaryAi, Integer length) {
		return summaryAi.length() > length ? summaryAi.substring(0, length) + "..." : summaryAi;
	}

	public static PrivatePostResponse toResponse(PrivatePost privatePost) {
		return new PrivatePostResponse(privatePost.getId(), privatePost.getTitle(), privatePost.getSummaryAi(),
			privatePost.getStancePlaintiff(), privatePost.getStanceDefendant(),
			privatePost.getFaultRatePlaintiff(), privatePost.getFaultRateDefendant(),
			privatePost.getJudgement(), privatePost.getPublished());
	}

	public static SummaryAndJudgementRequest toSummaryAndJudgementRequest(
		JudgementToAiParameter parameter, Member member) {
		return new SummaryAndJudgementRequest(
			parameter.content(),
			member.getNickname(),
			member.getGender(),
			member.getBirthDate());
	}

	public static JudgementResponse toJudgementResponse(
		JudgementFromAiResponse judgementFromAi,
		Integer faultRatePlaintiff,
		Integer faultRateDefendant,
		JudgementToAiParameter parameter) {
		return new JudgementResponse(
			judgementFromAi.title(),
			judgementFromAi.summaryAi(),
			judgementFromAi.stancePlaintiff(),
			judgementFromAi.stanceDefendant(),
			judgementFromAi.judgement(),
			100 - faultRatePlaintiff,
			100 - faultRateDefendant,
			parameter.originType()
		);
	}

	public static JudgementParameter toJudgementParameter(JudgementResponse response) {
		Long memberId = MemberLoader.getMemberId();
		return new JudgementParameter(
			memberId,
			response.title(),
			response.summary(),
			response.stancePlaintiff(),
			response.stanceDefendant(),
			response.judgement(),
			response.faultRatePlaintiff(),
			response.faultRateDefendant(),
			response.originType()
		);
	}

	public static JudgementToAiParameter toJudgementToAiParameter(JudgementToAiRequest judgementToAiRequest) {
		return new JudgementToAiParameter(
			MemberLoader.getMemberId(),
			judgementToAiRequest.content(),
			judgementToAiRequest.originType()
		);
	}

	public static FindPrivatePostParameter toFindPrivatePostParameter(FindPrivatePostRequest request) {
		return new FindPrivatePostParameter(
			MemberLoader.getMemberId(),
			request.privatePostId());
	}

	public static FindPrivatePostPreviewParameter toFindPrivatepostPreviewParameter(Pageable pageable) {
		return new FindPrivatePostPreviewParameter(
			MemberLoader.getMemberId(),
			pageable);
	}

	public static DeletePrivatePostParameter toDeletePrivatePostParameter(DeletePrivatePostRequest request) {
		return new DeletePrivatePostParameter(
			MemberLoader.getMemberId(),
			request.privatePostId());
	}

	public static DeletePrivatePostParameter toDeletePrivatePostParameter(Long privatePostId) {
		return new DeletePrivatePostParameter(MemberLoader.getMemberId(), privatePostId);
	}
}
