package aimo.backend.domains.privatePost.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.privatePost.service.AudioRecordService;
import aimo.backend.domains.privatePost.service.TextRecordService;
import aimo.backend.domains.privatePost.service.PrivatePostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/private-post")
@RequiredArgsConstructor
public class PrivatePost {

	private final AudioRecordService audioRecordService;
	private final PrivatePostService privatePostService;
	private final TextRecordService textRecordService;

	@PostMapping("/audio")
	public ResponseEntity<DataResponse<Void>> uploadAudio(@RequestParam("file") MultipartFile file) {

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}
}
