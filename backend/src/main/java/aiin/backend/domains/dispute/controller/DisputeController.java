package aiin.backend.domains.dispute.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aiin.backend.common.dto.DataResponse;
import aiin.backend.domains.dispute.service.AudioService;
import aiin.backend.domains.dispute.service.DialogueService;
import aiin.backend.domains.dispute.service.DisputeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/dispute")
@RequiredArgsConstructor
public class DisputeController {

	private final AudioService audioService;
	private final DisputeService disputeService;
	private final DialogueService dialogueService;

	@PostMapping("/audio")
	public ResponseEntity<DataResponse<Void>> uploadAudio(@RequestParam("file") MultipartFile file) {


		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

}
