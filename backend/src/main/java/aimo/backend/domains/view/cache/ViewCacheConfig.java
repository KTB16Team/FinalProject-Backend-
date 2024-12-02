package aimo.backend.domains.view.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ViewCacheConfig {

	@Bean
	public Map<Long, AtomicInteger> postViewsCount() {
		return new ConcurrentHashMap<>();
	}
}
