package aimo.backend.domains.member.entity;

import static lombok.AccessLevel.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@RedisHash(value = "accessToken", timeToLive = 3600)
public class AccessToken {
	@Id
	private Long memberId;
	@Indexed
	private String accessToken;
}
