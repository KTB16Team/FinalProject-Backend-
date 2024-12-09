package aimo.backend.domains.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.entity.MemberAttemptCount;
import aimo.backend.domains.member.model.IncreasePoint;
import aimo.backend.domains.member.repository.MemberAttemptCountRepository;
import aimo.backend.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberPointService {

	private final MemberRepository memberRepository;
	private final MemberAttemptCountRepository memberAttemptCountRepository;

	@Transactional
	public void checkAndIncreaseMemberPoint(Long memberId, IncreasePoint increasePoint) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));
		MemberAttemptCount memberAttemptCount = memberAttemptCountRepository.findById(memberId)
			.orElse(MemberAttemptCount.createBasic(memberId));

		// 하루 포인트 받을 수 있는 횟수 확인 후 포인트 증가
		if (memberAttemptCount.canReceivePoint(increasePoint)) {
			memberAttemptCount.increaseCount(increasePoint);
			member.increasePoint(increasePoint.getPoint());
		}

		memberAttemptCountRepository.save(memberAttemptCount);
	}
}
