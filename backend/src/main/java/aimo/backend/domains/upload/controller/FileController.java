package aimo.backend.domains.upload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.domains.upload.dto.parameter.CreateFilePreSignedUrlParameter;
import aimo.backend.domains.upload.dto.parameter.SaveFileMetaDataParameter;
import aimo.backend.domains.upload.dto.request.SaveFileMetaDataRequest;
import aimo.backend.domains.upload.service.FileService;
import aimo.backend.infrastructure.s3.dto.response.CreatePreSignedUrlResponse;
import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {

	private final FileService fileService;

	@PostMapping
	public ResponseEntity<DataResponse<Void>> saveFileMetaData(
		@Valid @RequestBody SaveFileMetaDataRequest request
	) {
		SaveFileMetaDataParameter parameter = SaveFileMetaDataParameter.from(request);
		fileService.save(parameter);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	@GetMapping("/presigned")
	public ResponseEntity<DataResponse<CreatePreSignedUrlResponse>> createFilePreSignedUrl(
		@RequestParam("filename") String filename,
		@RequestParam("prefix") PreSignedUrlPrefix prefix,
		@RequestParam("extension") String extension
	) {
		CreateFilePreSignedUrlParameter parameter = CreateFilePreSignedUrlParameter.of(filename, extension, prefix);

		CreatePreSignedUrlResponse response = fileService.createFilePreSignedUrl(parameter);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created(response));
	}
}
