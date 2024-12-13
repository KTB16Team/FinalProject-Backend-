package aimo.backend.infrastructure.s3;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import aimo.backend.common.properties.S3Properties;
import aimo.backend.infrastructure.s3.dto.parameter.CreateResourceUrlParameter;
import aimo.backend.infrastructure.s3.dto.response.CreatePreSignedUrlResponse;
import aimo.backend.infrastructure.s3.model.PresignedUrlPrefix;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3 amazonS3Client;
	private final S3Properties s3Properties;

	// PreSignedUrl 생성
	public CreatePreSignedUrlResponse createPreSignedUrl(
		String filename,
		PresignedUrlPrefix prefix
	) {
		String path = createPath(prefix, filename);
		String url = createGeneratePreSignedUrlRequest(path);
		return new CreatePreSignedUrlResponse(url, filename);
	}

	// s3를 통해 PreSignedUrl 생성
	private String createGeneratePreSignedUrlRequest(String path) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
			s3Properties.getBucketName(), path)
			.withMethod(HttpMethod.PUT)
			.withExpiration(getPreSignedUrlExpiration());
		return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
	}

	// 파일 URL 생성
	public String getResourceUrl(CreateResourceUrlParameter createResourceUrlParameter) {
		String key = createResourceUrlParameter.prefix() + "/" + createResourceUrlParameter.filename() + "."
			+ createResourceUrlParameter.extension();
		return amazonS3Client.getUrl(s3Properties.getBucketName(), key).toString();
	}

	// PreSignedUrl 만료 시간 설정
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

	// 파일 형식 및 이름으로 저장
	private String createPath(PresignedUrlPrefix prefix, String fileName) {
		return String.format("%s/%s", prefix.getValue(), fileName);
	}
}
