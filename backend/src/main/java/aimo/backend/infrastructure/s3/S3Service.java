package aimo.backend.infrastructure.s3;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import aimo.backend.common.properties.S3Properties;
import aimo.backend.infrastructure.s3.dto.parameter.CreateResourceUrlParameter;
import aimo.backend.infrastructure.s3.dto.response.CreatePreSignedUrlResponse;
import aimo.backend.infrastructure.s3.model.PreSignedUrlPrefix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3 amazonS3Client;
	private final S3Properties s3Properties;

	// PreSignedUrl 생성
	public CreatePreSignedUrlResponse createPreSignedUrl(
		String filename,
		PreSignedUrlPrefix prefix
	) {
		String key = prefix.getValue() + "/" + UUID.randomUUID() + filename;
		return new CreatePreSignedUrlResponse(getPreSignedUrl(key), filename, key);
	}

	// PreSignedUrl 생성
	private String getPreSignedUrl(String key) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = createGeneratePreSignedUrlRequest(key);
		URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}

	// s3 접근 url 생성
	public String getUrl(String key) {
		return amazonS3Client.getUrl(s3Properties.getBucketName(), key).toString();
	}

	// s3를 통해 PreSignedUrl 생성 요청
	private GeneratePresignedUrlRequest createGeneratePreSignedUrlRequest(String key) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest =
			new GeneratePresignedUrlRequest(s3Properties.getBucketName(), key)
				.withMethod(HttpMethod.PUT)
				.withExpiration(getPreSignedUrlExpiration());
		return generatePresignedUrlRequest;
	}

	// PreSignedUrl 만료 시간 설정
	private Date getPreSignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 2;
		expiration.setTime(expTimeMillis);
		return expiration;
	}

	// 파일 URL 생성
	public String getResourceUrl(String prefix, String filename, String extension) {
		String key = prefix + "/" + filename + "." + extension;

		return amazonS3Client.getUrl(s3Properties.getBucketName(), key).toString();
	}


	// 파일 삭제
	public void deleteFile(String path) {
		amazonS3Client.deleteObject(s3Properties.getBucketName(), path);
	}

}
