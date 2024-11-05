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

import aimo.backend.domains.privatePost.dto.request.ChatRecordRequest;
import aimo.backend.domains.privatePost.dto.request.JudgementToAiRequest;
import aimo.backend.domains.privatePost.dto.request.SummaryAndJudgementRequest;
import aimo.backend.domains.privatePost.dto.response.JudgementResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostResponse;
import aimo.backend.domains.privatePost.dto.request.SaveAudioSuccessRequest;
import aimo.backend.domains.privatePost.dto.response.SaveAudioSuccessResponse;
import aimo.backend.domains.privatePost.dto.request.SpeachToTextRequest;
import aimo.backend.domains.privatePost.dto.response.SpeachToTextResponse;

import aimo.backend.domains.privatePost.dto.request.TextRecordRequest;
import aimo.backend.domains.privatePost.service.AudioRecordService;
import aimo.backend.domains.privatePost.service.ChatRecordService;
import aimo.backend.domains.privatePost.service.PrivatePostService;

import aimo.backend.infrastructure.s3.dto.CreatePresignedUrlRequest;
import aimo.backend.infrastructure.s3.dto.CreatePresignedUrlResponse;
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

	// 판결
	@PostMapping("/judgement")
	public ResponseEntity<DataResponse<Void>> summaryAndJudgment(@Valid @RequestBody JudgementToAiRequest judgementToAiRequest) {

		JudgementResponse judgementResponse = privatePostService.serveScriptToAi(judgementToAiRequest);

		privatePostService.save(judgementResponse);

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	// 대화록 업로드
	@PostMapping("/text")
	public ResponseEntity<DataResponse<Void>> uploadTextRecord(
		@Valid @RequestBody TextRecordRequest textRecordRequest) {
		textRecordService.save(textRecordRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@PostMapping("/chat")
	public ResponseEntity<DataResponse<Void>> uploadChatRecord(
		@Valid @RequestParam("chat_record") ChatRecordRequest chatRecordRequest) throws IOException {

		chatRecordService.save(chatRecordRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@PostMapping("/speech-to-text")
	public ResponseEntity<DataResponse<SpeachToTextResponse>> speechToText(
		@Valid @RequestBody SpeachToTextRequest speachToTextRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created(audioRecordService.speachToText(speachToTextRequest)));
	}

	@GetMapping("/audio/presigned")
	public ResponseEntity<DataResponse<CreatePresignedUrlResponse>> getPresignedUrlTo(
		@Valid @RequestBody CreatePresignedUrlRequest createPresignedUrlRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(audioRecordService.createPresignedUrl(createPresignedUrlRequest)));
	}

	@PostMapping("/audio/success")
	public ResponseEntity<DataResponse<SaveAudioSuccessResponse>> saveAudioRecord(
		@Valid @RequestBody SaveAudioSuccessRequest saveAudioSuccessRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created(audioRecordService.save(saveAudioSuccessRequest)));
	}

	// 개인글 조회
	@GetMapping("/{privatePostId}")
	public ResponseEntity<DataResponse<PrivatePostResponse>> findPrivatePost(
		@Valid @PathVariable Long privatePostId) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.from(privatePostService.findPrivatePostBy(privatePostId)));
	}

	@GetMapping
	public ResponseEntity<DataResponse<Page<PrivatePostPreviewResponse>>> findPrivatePostPage(
		@Valid @RequestParam(defaultValue = "0")  Integer page,
		@Valid @RequestParam(defaultValue = "10") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.from(privatePostService.findPrivatePostPreviewsBy(pageable)));
	}

	@DeleteMapping("/{privatePostId}")
	public ResponseEntity<DataResponse<Void>> deletePrivatePost(
		@Valid @PathVariable Long privatePostId) {
		privatePostService.deletePrivatePostBy(privatePostId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(DataResponse.noContent());
	}
}
