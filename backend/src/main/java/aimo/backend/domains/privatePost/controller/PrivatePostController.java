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
import org.springframework.web.multipart.MultipartFile;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.privatePost.dto.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.PrivatePostResponse;
import aimo.backend.domains.privatePost.dto.SaveAudioSuccessRequest;
import aimo.backend.domains.privatePost.dto.SummaryAndJudgementResponse;
import aimo.backend.domains.privatePost.dto.TextRecordRequest;
import aimo.backend.domains.privatePost.service.AudioRecordService;
import aimo.backend.domains.privatePost.service.ChatRecordService;
import aimo.backend.domains.privatePost.service.PrivatePostService;
import aimo.backend.domains.privatePost.service.TextRecordService;
import aimo.backend.util.memberLoader.MemberLoader;
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
	private final MemberLoader memberLoader;

	@GetMapping("/upload/audio/presigned")
	public ResponseEntity<DataResponse<Void>> getPresignedUrlTo(@RequestParam("file") MultipartFile file) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/upload/audio/success")
	public ResponseEntity<DataResponse<Void>> saveAudioRecord(
		@RequestBody SaveAudioSuccessRequest saveAudioSuccessRequest
	) {
		audioRecordService.save(saveAudioSuccessRequest);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/judgement")
	public ResponseEntity<DataResponse<Void>> judgement(
		@RequestBody TextRecordRequest textRecordRequest
	) {

		SummaryAndJudgementResponse summaryAndJudgementResponse =
			privatePostService.serveScriptToAi(textRecordRequest);

		privatePostService.save(summaryAndJudgementResponse);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/upload/text")
	public ResponseEntity<DataResponse<Void>> uploadTextRecord(
		@RequestBody TextRecordRequest textRecordRequest
	) {
		textRecordService.save(textRecordRequest);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/upload/chat")
	public ResponseEntity<DataResponse<Void>> uploadChatRecord(
		@RequestParam("chat_record") MultipartFile file
	) throws IOException {
		chatRecordService.save(file);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@GetMapping("/{privatePostId}")
	public ResponseEntity<DataResponse<PrivatePostResponse>> getPrivatePost(
		@PathVariable Long privatePostId
	) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.from(privatePostService.getPrivatePost(privatePostId)));
	}

	@GetMapping("?page={page_number}&size={size}")
	public ResponseEntity<DataResponse<Page<PrivatePostPreviewResponse>>> getPrivatePostPage(
		@RequestParam(defaultValue = "0", name = "page_number") Long pageNumber,
		@RequestParam(defaultValue = "10") Long size
	) {
		Pageable pageable = PageRequest
			.of(pageNumber.intValue(),
				size.intValue(),
				Sort.by("createdAt").descending());

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.from(privatePostService.getPrivatePostPreviews(pageable)));
	}
}
