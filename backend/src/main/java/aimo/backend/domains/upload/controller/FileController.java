package aimo.backend.domains.upload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.upload.dto.parameter.CreateFilePreSignedUrlParameter;
import aimo.backend.domains.upload.dto.parameter.SaveFileMetaDataParameter;
import aimo.backend.domains.upload.dto.parameter.SaveProfileImageMetaDataParameter;
import aimo.backend.domains.upload.dto.request.SaveFileMetaDataRequest;
import aimo.backend.domains.upload.dto.request.SaveProfileImageMetaDataRequest;
import aimo.backend.domains.upload.dto.response.FindProfileImageUrlResponse;
import aimo.backend.domains.upload.dto.response.SaveFileMetaDataResponse;
import aimo.backend.domains.upload.service.FileService;
import aimo.backend.infrastructure.s3.dto.response.CreatePreSignedUrlResponse;
import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {

	private final FileService fileService;

	@PostMapping
	public ResponseEntity<DataResponse<SaveFileMetaDataResponse>> saveFileMetaData(
		@Valid @RequestBody SaveFileMetaDataRequest request
	) {
		SaveFileMetaDataParameter parameter = SaveFileMetaDataParameter.from(request);
		SaveFileMetaDataResponse response = fileService.saveFileMetaData(parameter);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.from(response));
	}

	// 파일 업로드 전 사전 서명된 URL 생성
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

	// 프로필 이미지 메타데이터 저장
	@PostMapping("/profile")
	public ResponseEntity<DataResponse<Void>> saveProfileImageMetaData(
		@RequestBody @Valid SaveProfileImageMetaDataRequest request
	) {
		Long memberId = MemberLoader.getMemberId();

		SaveProfileImageMetaDataParameter parameter = SaveProfileImageMetaDataParameter.from(request, memberId);
		fileService.saveProfileImageMetaData(parameter);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.created());
	}

	// 프로필 이미지 삭제
	@DeleteMapping("/profile")
	public ResponseEntity<DataResponse<Void>> deleteProfileImage() {
		Long memberId = MemberLoader.getMemberId();

		fileService.deleteProfileImage(memberId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(DataResponse.noContent());
	}

	// 프로필 이미지 조회
	@GetMapping("/profile")
	public ResponseEntity<DataResponse<FindProfileImageUrlResponse>> findProfileImageUrl() {
		Long memberId = MemberLoader.getMemberId();

		FindProfileImageUrlResponse response = fileService.findProfileImageUrl(memberId);

		return ResponseEntity.ok(DataResponse.from(response));
	}
}
