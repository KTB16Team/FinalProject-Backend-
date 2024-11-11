package aimo.backend.domains.privatePost.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;

import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.privatePost.dto.parameter.ChatRecordParameter;
import aimo.backend.domains.privatePost.dto.parameter.DeletePrivatePostParameter;
import aimo.backend.domains.privatePost.dto.parameter.FindPrivatePostParameter;
import aimo.backend.domains.privatePost.dto.parameter.FindPrivatePostPreviewParameter;
import aimo.backend.domains.privatePost.dto.parameter.SpeechToTextParameter;
import aimo.backend.domains.privatePost.dto.parameter.TextRecordParameter;
import aimo.backend.domains.privatePost.dto.request.DeletePrivatePostRequest;
import aimo.backend.domains.privatePost.dto.request.FindPrivatePostRequest;
import aimo.backend.domains.privatePost.dto.parameter.JudgementParameter;
import aimo.backend.domains.privatePost.dto.parameter.JudgementToAiParameter;
import aimo.backend.domains.privatePost.dto.request.ChatRecordRequest;
import aimo.backend.domains.privatePost.dto.request.JudgementToAiRequest;
import aimo.backend.domains.privatePost.dto.response.JudgementResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostResponse;
import aimo.backend.domains.privatePost.dto.request.SaveAudioSuccessRequest;
import aimo.backend.domains.privatePost.dto.response.SaveAudioSuccessResponse;
import aimo.backend.domains.privatePost.dto.request.SpeechToTextRequest;
import aimo.backend.domains.privatePost.dto.response.SavePrivatePostResponse;
import aimo.backend.domains.privatePost.dto.response.SpeechToTextResponse;

import aimo.backend.domains.privatePost.dto.request.TextRecordRequest;
import aimo.backend.domains.privatePost.service.AudioRecordService;
import aimo.backend.domains.privatePost.service.ChatRecordService;
import aimo.backend.domains.privatePost.service.PrivatePostService;

import aimo.backend.domains.privatePost.service.SaveAudioSuccessParameter;
import aimo.backend.infrastructure.s3.S3Service;
import aimo.backend.infrastructure.s3.dto.request.CreatePresignedUrlRequest;
import aimo.backend.infrastructure.s3.dto.response.CreatePresignedUrlResponse;
import jakarta.validation.Valid;

import aimo.backend.domains.privatePost.service.TextRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/private-posts")
@RequiredArgsConstructor
public class PrivatePostController {

	private final AudioRecordService audioRecordService;
	private final PrivatePostService privatePostService;
	private final TextRecordService textRecordService;
	private final ChatRecordService chatRecordService;
	private final S3Service s3Service;
	private final MemberLoader memberLoader;

	// 판결
	@PostMapping("/judgement")
	public ResponseEntity<DataResponse<SavePrivatePostResponse>> summaryAndJudgment(
		@Valid @RequestBody JudgementToAiRequest judgementToAiRequest
	) {
		Long memberId = MemberLoader.getMemberId();
		JudgementToAiParameter parameter = JudgementToAiParameter.from(memberId, judgementToAiRequest);
		JudgementResponse judgementResponse = privatePostService.serveScriptToAi(parameter);
		JudgementParameter judgementParameter = JudgementParameter.from(memberId, judgementResponse);
		Long privatePostId = privatePostService.save(judgementParameter);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(new SavePrivatePostResponse(privatePostId)));
	}

	// 대화록 업로드
	@PostMapping("/text")
	public ResponseEntity<DataResponse<Void>> uploadTextRecord(
		@Valid @RequestBody TextRecordRequest textRecordRequest
	) {
		TextRecordParameter parameter = TextRecordParameter.from(textRecordRequest);
		textRecordService.save(parameter);
		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@PostMapping("/chat")
	public ResponseEntity<DataResponse<Void>> uploadChatRecord(
		@Valid @RequestParam("chat_record") ChatRecordRequest chatRecordRequest
	) throws IOException {
		ChatRecordParameter parameter = ChatRecordParameter.from(chatRecordRequest);
		chatRecordService.save(parameter);
		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@PostMapping("/speech-to-text")
	public ResponseEntity<DataResponse<SpeechToTextResponse>> speechToText(
		@Valid @RequestBody SpeechToTextRequest speechToTextRequest
	) {
		SpeechToTextParameter parameter = SpeechToTextParameter.from(speechToTextRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(audioRecordService.speechToText(parameter)));
	}

	@GetMapping("/audio/presigned/{filename}")
	public ResponseEntity<DataResponse<CreatePresignedUrlResponse>> getPresignedUrlTo(
		@Valid @PathVariable("filename") CreatePresignedUrlRequest createPresignedUrlRequest
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(s3Service.createAudioPreSignedUrl(createPresignedUrlRequest)));
	}

	@PostMapping("/audio/success")
	public ResponseEntity<DataResponse<SaveAudioSuccessResponse>> saveAudioRecord(
		@Valid @RequestBody SaveAudioSuccessRequest saveAudioSuccessRequest
	) {
		SaveAudioSuccessParameter parameter = SaveAudioSuccessParameter.from(saveAudioSuccessRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(audioRecordService.save(parameter)));
	}

	// 개인글 조회
	@GetMapping("/{privatePostId}")
	public ResponseEntity<DataResponse<PrivatePostResponse>> findPrivatePost(
		@Valid @PathVariable("privatePostId") FindPrivatePostRequest request
	) {
		Long memberId = MemberLoader.getMemberId();
		FindPrivatePostParameter parameter = FindPrivatePostParameter.from(memberId, request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.from(privatePostService.findPrivatePostResponseBy(parameter)));
	}

	@GetMapping
	public ResponseEntity<DataResponse<Page<PrivatePostPreviewResponse>>> findPrivatePostPage(
		@Valid @RequestParam(defaultValue = "0") Integer page,
		@Valid @RequestParam(defaultValue = "10") Integer size
	) {
		Long memberId = MemberLoader.getMemberId();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		FindPrivatePostPreviewParameter parameter = FindPrivatePostPreviewParameter.of(memberId, pageable);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.from(privatePostService.findPrivatePostPreviewsBy(parameter)));
	}

	@DeleteMapping("/{privatePostId}")
	public ResponseEntity<DataResponse<Void>> deletePrivatePost(
		@Valid @PathVariable("privatePostId") DeletePrivatePostRequest request
	) {
		Long memberId = MemberLoader.getMemberId();
		DeletePrivatePostParameter parameter = DeletePrivatePostParameter.from(memberId, request);
		privatePostService.deletePrivatePostBy(parameter);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(DataResponse.noContent());
	}
}
