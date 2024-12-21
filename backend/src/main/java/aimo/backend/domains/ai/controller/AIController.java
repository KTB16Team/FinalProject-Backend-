package aimo.backend.domains.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.ai.dto.parameter.ImageToTextParameter;
import aimo.backend.domains.ai.dto.parameter.UploadFileRecordAndJudgementParameter;
import aimo.backend.domains.ai.dto.parameter.UploadTextRecordAndJudgementParameter;
import aimo.backend.domains.ai.dto.parameter.SpeechToTextParameter;
import aimo.backend.domains.ai.dto.request.ImageToTextCallbackRequest;
import aimo.backend.domains.ai.dto.request.ImageToTextRequest;
import aimo.backend.domains.ai.dto.request.SpeechToTextCallbackRequest;
import aimo.backend.domains.ai.dto.request.SpeechToTextRequest;
import aimo.backend.domains.ai.dto.request.UpdateContentToPrivatePostRequest;
import aimo.backend.domains.ai.service.AIService;
import aimo.backend.domains.privatePost.dto.parameter.UpdateContentToPrivatePostParameter;
import aimo.backend.domains.privatePost.dto.request.UploadTextRecordAndRequestJudgementRequest;
import aimo.backend.domains.privatePost.model.OriginType;
import aimo.backend.domains.privatePost.service.PrivatePostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AIController {

	private final AIService aiService;
	private final PrivatePostService privatePostService;

	@PostMapping("/private-posts/speech-to-text")
	public ResponseEntity<DataResponse<Void>> speechToText(
		@Valid @RequestBody SpeechToTextRequest request
	) {
		SpeechToTextParameter parameter = SpeechToTextParameter.of(request.url(), MemberLoader.getMemberId());
		aiService.speechToText(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	// AI로부터의 음성인식 콜백
	@PostMapping("/private-posts/speech-to-text/callback")
	public ResponseEntity<DataResponse<Void>> speechToTextCallback(
		@Valid @RequestBody SpeechToTextCallbackRequest request
	) {
		UploadFileRecordAndJudgementParameter parameter = new UploadFileRecordAndJudgementParameter(
			request.id(),
			request.script(),
			OriginType.VOICE,
			request.privatePostId());

		aiService.uploadFileRecordAndJudgement(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/private-posts/image-to-text")
	public ResponseEntity<DataResponse<Void>> ImageToText(
		@Valid @RequestBody ImageToTextRequest request
	) {
		ImageToTextParameter parameter = ImageToTextParameter.from(request.url(), MemberLoader.getMemberId());
		aiService.imageToText(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/private-posts/image-to-text/callback")
	public ResponseEntity<DataResponse<Void>> imageToTextCallback(
		@Valid @RequestBody ImageToTextCallbackRequest request
	) {
		UploadFileRecordAndJudgementParameter parameter = new UploadFileRecordAndJudgementParameter(
			request.id(),
			request.script(),
			OriginType.IMAGE,
			request.privatePostId());

		aiService.uploadFileRecordAndJudgement(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/private-posts/judgement/text")
	public ResponseEntity<DataResponse<Void>> uploadTextRecordAndJudgement(
		@Valid @RequestBody UploadTextRecordAndRequestJudgementRequest request
	) {
		Long memberId = MemberLoader.getMemberId();

		UploadTextRecordAndJudgementParameter uploadTextRecordAndJudgementParameter = new UploadTextRecordAndJudgementParameter(
			memberId,
			request.content(),
			OriginType.TEXT);

		aiService.uploadTextRecordAndJudgement(uploadTextRecordAndJudgementParameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	// AI로부터의 판결문 요청 콜백
	@PostMapping("/private-posts/judgement/callback")
	public ResponseEntity<DataResponse<Void>> updateContentToPrivatePost(
		@Valid @RequestBody UpdateContentToPrivatePostRequest request
	) {
		UpdateContentToPrivatePostParameter parameter = UpdateContentToPrivatePostParameter.from(request);
		privatePostService.updateContentToPrivatePost(parameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/private-posts/analyze-conflict")
	public ResponseEntity<DataResponse<Void>> uploadImageOrSoundRecordAndJudgement(
		@Valid @RequestBody UploadTextRecordAndRequestJudgementRequest request
	) {
		Long memberId = MemberLoader.getMemberId();

		UploadTextRecordAndJudgementParameter uploadTextRecordAndJudgementParameter = new UploadTextRecordAndJudgementParameter(
			memberId,
			request.content(),
			OriginType.TEXT);

		aiService.uploadTextRecordAndJudgement(uploadTextRecordAndJudgementParameter);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
