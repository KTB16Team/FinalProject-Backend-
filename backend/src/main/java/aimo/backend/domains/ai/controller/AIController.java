package aimo.backend.domains.ai.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.ai.dto.parameter.ImageToTextParameter;
import aimo.backend.domains.ai.dto.parameter.JudgementToAiParameter;
import aimo.backend.domains.ai.dto.parameter.SpeechToTextParameter;
import aimo.backend.domains.ai.dto.request.ImageToTextRequest;
import aimo.backend.domains.ai.dto.request.SpeechToTextRequest;
import aimo.backend.domains.ai.dto.response.ImageToTextResponse;
import aimo.backend.domains.ai.dto.response.SpeechToTextResponse;
import aimo.backend.domains.ai.service.AIService;
import aimo.backend.domains.privatePost.dto.parameter.UpdateContentToPrivatePostParameter;
import aimo.backend.domains.privatePost.dto.request.UpdateContentToPrivatePostRequest;
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
	public ResponseEntity<DataResponse<SpeechToTextResponse>> speechToText(
		@Valid @RequestBody SpeechToTextRequest request
	) {
		SpeechToTextParameter parameter = SpeechToTextParameter.from(request);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(aiService.speechToText(parameter)));
	}

	@PostMapping("/private-posts/image-to-text")
	public ResponseEntity<DataResponse<ImageToTextResponse>> ImageToText(
		@Valid @RequestBody ImageToTextRequest request
	) {
		ImageToTextParameter parameter = ImageToTextParameter.from(request.url());

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(aiService.imageToText(parameter)));
	}

	@PostMapping("/private-posts/judgement/text")
	public ResponseEntity<DataResponse<Void>> uploadTextRecordAndJudgement(
		@Valid @RequestBody UploadTextRecordAndRequestJudgementRequest request
	) {
		Long memberId = MemberLoader.getMemberId();

		JudgementToAiParameter judgementToAiParameter = JudgementToAiParameter.of(
			memberId,
			request.content(),
			OriginType.TEXT);

		aiService.uploadTextRecordAndRequestJudgement(judgementToAiParameter);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/private-posts/analyze-conflict")
	public ResponseEntity<DataResponse<Void>> uploadImageOrSoundRecordAndJudgement(
		@Valid @RequestBody UploadTextRecordAndRequestJudgementRequest request
	) {
		Long memberId = MemberLoader.getMemberId();

		JudgementToAiParameter judgementToAiParameter = JudgementToAiParameter.of(
			memberId,
			request.content(),
			OriginType.TEXT);

		aiService.uploadImageOrSoundRecordAndJudgement(judgementToAiParameter);

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
}
