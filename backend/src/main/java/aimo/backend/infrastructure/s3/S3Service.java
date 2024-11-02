package aimo.backend.infrastructure.s3;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import aimo.backend.common.properties.S3Properties;
import aimo.backend.domains.member.dto.CreateProfileImageUrlRequest;
import aimo.backend.infrastructure.s3.dto.CreatePresignedUrlRequest;
import aimo.backend.infrastructure.s3.model.PresignedUrlPrefix;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3Client amazonS3Client;
	private final S3Properties s3Properties;

	public String createAudioPreSignedUrl(CreatePresignedUrlRequest request) {
		String path = createPath(PresignedUrlPrefix.AUDIO.getValue(), request.fileName());
		return createGeneratePresignedUrlRequest(path);
	}

	public CreatePresignedUrlResponse createProfilePresignedUrl(CreateProfileImageUrlRequest request) {
		String path = createPath(PresignedUrlPrefix.IMAGE.getValue(), request.memberName() + "." + request.extension());
		String url = createGeneratePresignedUrlRequest(path);
		String filename = request.memberName() + "." + request.extension();
		return new CreatePresignedUrlResponse(url, filename);
	}

	private String createGeneratePresignedUrlRequest(String path) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
			s3Properties.getBucketName(), path)
			.withMethod(HttpMethod.PUT)
			.withExpiration(getPreSignedUrlExpiration());

		generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL,
			CannedAccessControlList.PublicRead.toString());

		URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

		return url.toString();
	}

	private Date getPreSignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 2;
		expiration.setTime(expTimeMillis);
		return expiration;
	}

	/**
	 * 파일 고유 ID를 생성
	 *
	 * @return 36자리의 UUID
	 */
	private String createFileId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 파일의 전체 경로를 생성
	 *
	 * @param prefix 디렉토리 경로
	 * @return 파일의 전체 경로
	 */
	private String createPath(String prefix, String fileName) {
		String fileId = createFileId();
		return String.format("%s/%s", prefix, fileId + fileName);
	}
}
