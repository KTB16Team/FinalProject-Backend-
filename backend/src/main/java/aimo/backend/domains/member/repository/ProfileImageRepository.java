package aimo.backend.domains.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.member.entity.ProfileImage;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
	Optional<ProfileImage> findByMemberId(Long memberId);
	void deleteProfileImageByMemberId(Long memberId);
}
