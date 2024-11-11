package aimo.backend.infrastructure.s3;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import aimo.backend.common.properties.S3Properties;
import aimo.backend.infrastructure.s3.dto.parameter.CreateResourceUrlParameter;
import aimo.backend.infrastructure.s3.dto.request.CreatePresignedUrlRequest;
import aimo.backend.infrastructure.s3.dto.response.CreatePresignedUrlResponse;
import aimo.backend.infrastructure.s3.model.PresignedUrlPrefix;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3 amazonS3Client;
	private final S3Properties s3Properties;

	public CreatePresignedUrlResponse createAudioPreSignedUrl(CreatePresignedUrlRequest request) {
		String path = createPath(PresignedUrlPrefix.AUDIO.getValue(), request.filename());
		String url = createGeneratePresignedUrlRequest(path);
		return new CreatePresignedUrlResponse(url, request.filename());
	}

	public CreatePresignedUrlResponse createProfilePresignedUrl(CreatePresignedUrlRequest request) {
		String path = createPath(PresignedUrlPrefix.IMAGE.getValue(), request.filename());
		String url = createGeneratePresignedUrlRequest(path);
		return new CreatePresignedUrlResponse(url, request.filename());
	}

	private String createGeneratePresignedUrlRequest(String path) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
			s3Properties.getBucketName(), path)
			.withMethod(HttpMethod.PUT)
			.withExpiration(getPreSignedUrlExpiration());
		return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
	}

	public String getResourceUrl(CreateResourceUrlParameter createResourceUrlParameter) {
		String key = createResourceUrlParameter.prefix() + "/" + createResourceUrlParameter.filename() + "." + createResourceUrlParameter.extension();
		return amazonS3Client.getUrl(s3Properties.getBucketName(), key).toString();
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
		return String.format("%s/%s", prefix, fileName);
	}
}
