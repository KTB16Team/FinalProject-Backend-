package aimo.backend.domains.member.entity;

import static lombok.AccessLevel.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@RedisHash(value = "refreshToken", timeToLive = 1209600)
public class RefreshToken {

	@Id
	private Long memberId;
	@Indexed
	@Setter
	private String refreshToken;
}
