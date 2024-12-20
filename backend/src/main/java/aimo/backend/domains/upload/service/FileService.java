package aimo.backend.domains.upload.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aimo.backend.common.exception.ApiException;
import aimo.backend.common.exception.ErrorCode;
import aimo.backend.common.properties.AiServerProperties;
import aimo.backend.common.util.webclient.ReactiveHttpService;
import aimo.backend.domains.member.entity.Member;
import aimo.backend.domains.member.repository.MemberRepository;
import aimo.backend.domains.privatePost.dto.parameter.ImageToTextParameter;
import aimo.backend.domains.privatePost.dto.response.ImageToTextResponse;
import aimo.backend.domains.privatePost.dto.response.SpeechToTextResponse;
import aimo.backend.domains.upload.dto.parameter.CreateFilePreSignedUrlParameter;
import aimo.backend.domains.upload.dto.parameter.SaveFileMetaDataParameter;
import aimo.backend.domains.upload.dto.parameter.SaveProfileImageMetaDataParameter;
import aimo.backend.domains.privatePost.dto.parameter.SpeechToTextParameter;
import aimo.backend.domains.upload.dto.response.FindProfileImageUrlResponse;
import aimo.backend.domains.upload.dto.response.SaveFileMetaDataResponse;
import aimo.backend.domains.upload.entity.FileRecord;
import aimo.backend.domains.upload.entity.ProfileImage;
import aimo.backend.domains.upload.repository.FileRepository;
import aimo.backend.domains.upload.repository.ProfileImageRepository;
import aimo.backend.infrastructure.s3.S3Service;
import aimo.backend.infrastructure.s3.dto.response.CreatePreSignedUrlResponse;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

	private final FileRepository fileRepository;
	private final ReactiveHttpService reactiveHttpService;
	private final AiServerProperties aiServerProperties;
	private final S3Service s3Service;
	private final MemberRepository memberRepository;
	private final ProfileImageRepository profileImageRepository;

	// AI서버에 음성 파일을 텍스트로 변환 요청
	public SpeechToTextResponse speechToText(SpeechToTextParameter speechToTextParameter) {
		String url = aiServerProperties.getDomainUrl() + aiServerProperties.getSpeechToTextApi();
		return reactiveHttpService.post(url, speechToTextParameter, SpeechToTextResponse.class).block();
	}

	// AI서버에 이미지 파일을 텍스트로 변환 요청
	public ImageToTextResponse imageToText(ImageToTextParameter parameter) {
		String url = aiServerProperties.getDomainUrl() + aiServerProperties.getImageToTextApi();
		return reactiveHttpService.post(url, parameter, ImageToTextResponse.class).block();
	}

	// 음성 파일 메타데이터 저장
	@Transactional(rollbackFor = ApiException.class)
	public SaveFileMetaDataResponse saveFileMetaData(SaveFileMetaDataParameter parameter) {
		// url 생성
		String url = s3Service.getUrl(parameter.key());

		fileRepository.save(FileRecord.of(parameter, url));

		return new SaveFileMetaDataResponse(url);
	}

	// 파일 업로드 전 사전 서명된 URL 생성
	public CreatePreSignedUrlResponse createFilePreSignedUrl(CreateFilePreSignedUrlParameter parameter) {
		// Prefix 타입과 extention 일치하는 지 확인

		if (!parameter.prefix().isValidExtension(parameter.extension())) {
			throw ApiException.from(ErrorCode.INVALID_FILE_EXTENSION);
		}

		return s3Service.createPreSignedUrl(
			parameter.filename(),
			parameter.prefix()
		);
	}


	// 프로필 이미지 메타데이터 저장
	@Transactional(rollbackFor = ApiException.class)
	public void saveProfileImageMetaData(SaveProfileImageMetaDataParameter parameter) {
		Member member = memberRepository.findById(parameter.memberId())
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		// 기존 프로필 이미지 삭제, nullPointException 방지
		if (member.getProfileImage() != null) {
			deleteProfileImage(parameter.memberId());
		}

		// url 생성
		String url = s3Service.getUrl(parameter.key());

		ProfileImage profileImage = ProfileImage.of(parameter, member, url);

		profileImageRepository.save(profileImage);
		member.updateProfileImage(profileImage);
	}

	// 프로필 이미지 삭제
	@Transactional(rollbackFor = ApiException.class)
	public void deleteProfileImage(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> ApiException.from(ErrorCode.MEMBER_NOT_FOUND));

		ProfileImage profileImage = member.getProfileImage();

		// s3 파일 삭제
		s3Service.deleteFile(profileImage.getKey());
		// DB 삭제
		profileImageRepository.delete(profileImage);
		member.updateProfileImage(null);
	}

	// 프로필 이미지 URL 조회
	public FindProfileImageUrlResponse findProfileImageUrl(Long memberId) {
		ProfileImage profileImage = profileImageRepository.findByMemberId(memberId)
			.orElseThrow(() -> ApiException.from(ErrorCode.PROFILE_IMAGE_NOT_FOUND));

		return FindProfileImageUrlResponse.from(profileImage.getUrl());
	}
}
