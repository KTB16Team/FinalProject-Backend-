package aimo.backend.domains.privatePost.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;

import aimo.backend.domains.privatePost.dto.ChatRecordRequest;
import aimo.backend.domains.privatePost.dto.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.PrivatePostResponse;
import aimo.backend.domains.privatePost.dto.SaveAudioSuccessResponse;
import aimo.backend.domains.privatePost.dto.SpeachToTextRequest;
import aimo.backend.domains.privatePost.dto.SpeachToTextResponse;
import aimo.backend.domains.privatePost.dto.SummaryAndJudgementResponse;
import aimo.backend.domains.privatePost.dto.TextRecordRequest;
import aimo.backend.domains.privatePost.dto.SaveAudioSuccessRequest;
import aimo.backend.domains.privatePost.service.AudioRecordService;
import aimo.backend.domains.privatePost.service.ChatRecordService;
import aimo.backend.domains.privatePost.service.TextRecordService;
import aimo.backend.domains.privatePost.service.PrivatePostService;
import aimo.backend.infrastructure.s3.dto.CreatePresignedUrlRequest;
import aimo.backend.infrastructure.s3.dto.CreatePresignedUrlResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/private-posts")
@RequiredArgsConstructor
public class PrivatePostController {

	private final AudioRecordService audioRecordService;
	private final PrivatePostService privatePostService;
	private final TextRecordService textRecordService;
	private final ChatRecordService chatRecordService;

	// 판결
	@PostMapping("/judgement")
	public ResponseEntity<DataResponse<Void>> judgement(@Valid @RequestBody TextRecordRequest textRecordRequest) {

		SummaryAndJudgementResponse summaryAndJudgementResponse = privatePostService.serveScriptToAi(textRecordRequest);

		privatePostService.save(summaryAndJudgementResponse);

		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	// 대화록 업로드
	@PostMapping("/upload/text")
	public ResponseEntity<DataResponse<Void>> uploadTextRecord(
		@Valid @RequestBody TextRecordRequest textRecordRequest) {
		textRecordService.save(textRecordRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created());
	}

	@PostMapping("/upload/chat")
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


	@GetMapping("/upload/audio/presigned")
	public ResponseEntity<DataResponse<CreatePresignedUrlResponse>> getPresignedUrlTo(
		@Valid @RequestBody CreatePresignedUrlRequest createPresignedUrlRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(audioRecordService.createPresignedUrl(createPresignedUrlRequest)));
	}

	@PostMapping("/upload/audio/success")
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

	@GetMapping("?page={page_number}&size={size}")
	public ResponseEntity<DataResponse<Page<PrivatePostPreviewResponse>>> findPrivatePostPage(
		@Valid @RequestParam(defaultValue = "0")  Long pageNumber,
		@Valid @RequestParam(defaultValue = "10") Long size) {
		Pageable pageable = PageRequest.of(pageNumber.intValue(), size.intValue(), Sort.by("createdAt").descending());

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.from(privatePostService.findPrivatePostPreviewsBy(pageable)));
	}
}
