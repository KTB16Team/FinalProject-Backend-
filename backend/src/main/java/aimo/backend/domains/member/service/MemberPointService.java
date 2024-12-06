package aimo.backend.domains.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberPointService {

	private final MemberRepository memberRepository;

	@Transactional
	public void increaseMemberPoint(Long memberId, int point) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		member.increasePoint(point);
	}
}
