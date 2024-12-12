package aimo.backend.common.scheduler;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.domains.member.repository.MemberAttemptCountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulingService {

	private final MemberAttemptCountRepository memberAttemptCountRepository;

	@Transactional
	@Async
	@Scheduled(cron = "0 0 0 * * ?") //매일 자정에 초기화
	public void deleteAllMemberPointLimitCount() {
		memberAttemptCountRepository.deleteAll();
	}
}
