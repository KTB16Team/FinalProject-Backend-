package aimo.backend.domains.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aimo.backend.common.dto.DataResponse;
import aimo.backend.common.util.memberLoader.MemberLoader;
import aimo.backend.domains.member.dto.response.FindMemberPointResponse;
import aimo.backend.domains.member.service.MemberPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/members/points")
@RequiredArgsConstructor
public class MemberPointController {

	private final MemberPointService memberPointService;

	// 멤버 포인트 조회
	@GetMapping
	public ResponseEntity<DataResponse<FindMemberPointResponse>> findMemberPoint() {
		Long memberId = MemberLoader.getMemberId();

		FindMemberPointResponse response = memberPointService.findMemberPoint(memberId);

		return ResponseEntity.ok(DataResponse.from(response));
	}
}
