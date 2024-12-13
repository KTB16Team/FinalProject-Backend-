package aimo.backend.domains.upload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.upload.dto.parameter.CreateAudioPreSignedUrlParameter;
import aimo.backend.domains.upload.dto.parameter.SaveAudioMetaDataParameter;
import aimo.backend.domains.upload.dto.request.SaveAudioMetaDataRequest;
import aimo.backend.domains.upload.dto.response.SaveAudioMetaDataResponse;
import aimo.backend.domains.upload.service.AudioRecordService;
import aimo.backend.infrastructure.s3.dto.request.CreatePreSignedUrlRequest;
import aimo.backend.infrastructure.s3.dto.response.CreatePreSignedUrlResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/audio")
public class AudioController {

	private final AudioRecordService audioRecordService;

	@PostMapping
	public ResponseEntity<DataResponse<SaveAudioMetaDataResponse>> saveAudioMetaData(
		@Valid @RequestBody SaveAudioMetaDataRequest request
	) {
		SaveAudioMetaDataParameter parameter = SaveAudioMetaDataParameter.from(request);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(audioRecordService.save(parameter)));
	}

	@GetMapping("/presigned/{filename}")
	public ResponseEntity<DataResponse<CreatePreSignedUrlResponse>> createAudioPreSignedUrl(
		@Valid @RequestBody CreatePreSignedUrlRequest request
	) {
		CreateAudioPreSignedUrlParameter parameter = CreateAudioPreSignedUrlParameter.of(request.filename());

		CreatePreSignedUrlResponse response = audioRecordService.createAudioPreSignedUrl(parameter);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(response));
	}
}
