package aimo.backend.domains.privatePost.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.dto.PageResponse;
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.privatePost.dto.parameter.DeletePrivatePostParameter;
import aimo.backend.domains.privatePost.dto.parameter.FindPrivatePostParameter;
import aimo.backend.domains.privatePost.dto.parameter.FindPrivatePostPreviewParameter;
import aimo.backend.domains.privatePost.dto.response.PrivatePostPreviewResponse;
import aimo.backend.domains.privatePost.dto.response.PrivatePostResponse;
import aimo.backend.domains.privatePost.service.PrivatePostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/private-posts")
@RequiredArgsConstructor
public class PrivatePostController {

	private final PrivatePostService privatePostService;

	// 개인글 조회
	@GetMapping("/{privatePostId}")
	public ResponseEntity<DataResponse<PrivatePostResponse>> findPrivatePost(
		@Valid @PathVariable Long privatePostId
	) {
		Long memberId = MemberLoader.getMemberId();

		FindPrivatePostParameter parameter = FindPrivatePostParameter.of(memberId, privatePostId);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DataResponse.from(privatePostService.findPrivatePostResponseBy(parameter)));
	}

	@GetMapping
	public ResponseEntity<DataResponse<PageResponse<PrivatePostPreviewResponse>>> findPrivatePostPage(
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
		@Valid @PathVariable("privatePostId") Long privatePostId
	) {
		Long memberId = MemberLoader.getMemberId();

		DeletePrivatePostParameter parameter = DeletePrivatePostParameter.of(memberId, privatePostId);

		privatePostService.deletePrivatePostBy(parameter);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(DataResponse.noContent());
	}
}
