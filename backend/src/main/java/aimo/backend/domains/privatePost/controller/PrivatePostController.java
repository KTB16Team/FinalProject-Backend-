package aimo.backend.domains.privatePost.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.mapper.PrivatePostMapper;
import aimo.backend.domains.auth.security.jwtFilter.JwtTokenProviderImpl;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.service.MemberService;
import aimo.backend.domains.privatePost.dto.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.PrivatePostResponse;
import aimo.backend.domains.privatePost.dto.SummaryAndJudgementRequest;
import aimo.backend.domains.privatePost.dto.TextRecordRequest;
import aimo.backend.domains.privatePost.dto.UploadAudioSuccessRequest;
import aimo.backend.domains.privatePost.entity.PrivatePost;
import aimo.backend.domains.privatePost.service.AudioRecordService;
import aimo.backend.domains.privatePost.service.ChatRecordService;
import aimo.backend.domains.privatePost.service.TextRecordService;
import aimo.backend.domains.privatePost.service.PrivatePostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/private-post")
@RequiredArgsConstructor
public class PrivatePostController {

	private final AudioRecordService audioRecordService;
	private final PrivatePostService privatePostService;
	private final TextRecordService textRecordService;
	private final ChatRecordService chatRecordService;
	private final MemberService memberService;
	private final JwtTokenProviderImpl jwtTokenProviderImpl;

	@GetMapping("/upload/audio/presigned")
	public ResponseEntity<DataResponse<Void>> getPresignedUrlTo(@RequestParam("file") MultipartFile file)  {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/upload/audio/success")
	public ResponseEntity<DataResponse<Void>> saveAudioRecord(
		@RequestBody UploadAudioSuccessRequest uploadAudioSuccessRequest
	) {
		audioRecordService.save(uploadAudioSuccessRequest);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@PostMapping("/judgement")
	public ResponseEntity<DataResponse<Void>> judgement(
		@RequestHeader("Authorization") String accessToken,
		@RequestBody TextRecordRequest textRecordRequest
	) {
		Long memberId = jwtTokenProviderImpl
			.extractMemberId(accessToken)
			.orElseThrow(() -> ApiException.from(ErrorCode.INVALID_TOKEN));
		
		Member member = memberService.findById(memberId);

		SummaryAndJudgementRequest summaryAndJudgementRequest =
			new SummaryAndJudgementRequest(
				textRecordRequest.title(),
				textRecordRequest.script(),
				member.getUsername(),
				member.getGender(),
				member.getBirthDate()
			);

		return privatePostService
			.serveScriptToAi(summaryAndJudgementRequest)
			.blockOptional()
			.map(response -> ResponseEntity.status(HttpStatus.CREATED).body(DataResponse.created()))
			.orElseThrow(() -> ApiException.from(ErrorCode.AI_BAD_GATEWAY));
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
