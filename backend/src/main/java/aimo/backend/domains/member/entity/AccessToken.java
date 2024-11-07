package aimo.backend.domains.member.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "accessToken", timeToLive = 3600)
public class AccessToken {
	@Id
	private Long memberId;
	@Indexed
	private String accessToken;
}