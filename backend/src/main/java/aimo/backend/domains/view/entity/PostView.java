package aimo.backend.domains.view.entity;

import static lombok.AccessLevel.*;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = PROTECTED)
@RedisHash(value = "postView" , timeToLive = 1209600)
public class PostView {

	@Id
	private String id;

	private Long postId;
	private Long memberId;

	@Builder
	private PostView(Long postId, Long memberId) {
		this.id = generateId(postId, memberId);
		this.postId = postId;
		this.memberId = memberId;
	}

	public static String generateId(Long postId, Long memberId) {
		return postId + ":" + memberId; // 고유 Key 생성
	}
}
