package aimo.backend.domains.like.entity;

import static jakarta.persistence.GenerationType.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import aimo.backend.common.entity.BaseEntity;
import aimo.backend.domains.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class Like extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(nullable = false)
	private Long like_id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	public Like(Member member) {
		this.member = member;
	}
}
