package aimo.backend.domains.like.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LikeCacheConfig {

	@Bean
	public Map<Long, AtomicInteger> postLikesCount() {
		return new ConcurrentHashMap<>();
	}
}
