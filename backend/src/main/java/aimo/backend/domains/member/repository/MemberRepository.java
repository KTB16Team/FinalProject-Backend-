package aimo.backend.domains.member.repository;

import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.model.Provider;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByEmailAndProvider(String email, Provider provider);

	Optional<Member> findByEmailAndProvider(String email, Provider provider);

	Optional<Member> findByProviderAndProviderId(Provider provider, String providerId);
}
