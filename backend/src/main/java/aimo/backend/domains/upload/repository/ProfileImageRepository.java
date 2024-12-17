package aimo.backend.domains.upload.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import aimo.backend.domains.upload.entity.ProfileImage;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
	Optional<ProfileImage> findByMemberId(Long memberId);
}
