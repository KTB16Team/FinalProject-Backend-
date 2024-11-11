package aimo.backend.domains.member.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@RedisHash(value = "accessToken", timeToLive = 3600)
public class AccessToken {
	@Id
	private final Long memberId;
	@Indexed
	private final String accessToken;
}
