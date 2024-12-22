package aimo.backend.domains.ai.service;

import org.springframework.amqp.AmqpException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.messageQueue.MessageQueueService;
import aimo.backend.domains.ai.dto.parameter.ImageToTextParameter;
import aimo.backend.domains.ai.dto.parameter.UploadFileRecordAndJudgementParameter;
import aimo.backend.domains.ai.dto.parameter.UploadTextRecordAndJudgementParameter;
import aimo.backend.domains.ai.dto.parameter.SpeechToTextParameter;
import aimo.backend.domains.ai.dto.request.AiImageToTextRequest;
import aimo.backend.domains.ai.dto.request.AiSpeechToTextRequest;
import aimo.backend.domains.ai.dto.request.SummaryAndJudgementRequest;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.privatePost.entity.TextRecord;
import aimo.backend.domains.privatePost.model.OriginType;
import aimo.backend.domains.privatePost.repository.PrivatePostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIService {

	private final MessageQueueService messageQueueService;
	private final MemberRepository memberRepository;
	private final PrivatePostRepository privatePostRepository;

	// mq에 텍스트 판단 요청
	@Async
	@Transactional(rollbackFor = {ApiException.class, AmqpException.class})
	public void uploadTextRecordAndJudgement(UploadTextRecordAndJudgementParameter parameter) {
		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		// db에 저장
		TextRecord textRecord = TextRecord.of(parameter.content());
		PrivatePost privatePost = PrivatePost.createWithoutContent(member, textRecord, parameter.originType());
		privatePostRepository.save(privatePost);

		// mq에 요청
		SummaryAndJudgementRequest request = SummaryAndJudgementRequest.from(
			privatePost.getId(),
			parameter.content(),
			member);
		messageQueueService.judgement(request);
	}

	// mq에 image or sound 판단 요청
	@Async
	@Transactional(rollbackFor = {ApiException.class, AmqpException.class})
	public void uploadFileRecordAndJudgement(UploadFileRecordAndJudgementParameter parameter) {
		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		// db에 저장
		PrivatePost privatePost = privatePostRepository.findById(parameter.privatePostId())
			.orElseThrow(() -> ApiException.from(ErrorCode.PRIVATE_POST_NOT_FOUND));
		privatePost.getTextRecord().setContent(parameter.content());

		// mq에 요청
		SummaryAndJudgementRequest request = SummaryAndJudgementRequest.from(
			parameter.privatePostId(),
			parameter.content(),
			member);
		messageQueueService.judgement(request);
	}

	// AI서버에 음성 파일을 텍스트로 변환 요청
	public void speechToText(SpeechToTextParameter parameter) {
		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		// db에 저장
		TextRecord textRecord = TextRecord.withoutContent();
		PrivatePost privatePost = PrivatePost.createWithoutContent(member, textRecord, OriginType.VOICE);
		privatePostRepository.save(privatePost);

		AiSpeechToTextRequest request = new AiSpeechToTextRequest(
			parameter.url(),
			parameter.memberId(),
			privatePost.getId());

		messageQueueService.speechToText(request);
	}

	// AI서버에 이미지 파일을 텍스트로 변환 요청
	public void imageToText(ImageToTextParameter parameter) {
		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		// db에 저장
		TextRecord textRecord = TextRecord.withoutContent();
		PrivatePost privatePost = PrivatePost.createWithoutContent(member, textRecord, OriginType.IMAGE);
		privatePostRepository.save(privatePost);

		AiImageToTextRequest request = new AiImageToTextRequest(
			parameter.url(),
			parameter.memberId(),
			privatePost.getId());

		messageQueueService.imageToText(request);
	}
}
